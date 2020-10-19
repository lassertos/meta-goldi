DESCRIPTION = "Generates a vfat image for the boot partition for use with RAUC"
LICENSE = "MIT"

inherit deploy nopackages

INHIBIT_DEFAULT_DEPS = "1"

# Set kernel and boot loader
IMAGE_BOOTLOADER ?= "bootfiles"

# Kernel image name
BOOTIMG_KERNELIMAGE ?= "${SDIMG_KERNELIMAGE}"

do_deploy[depends] = " \
    parted-native:do_populate_sysroot \
    mtools-native:do_populate_sysroot \
    dosfstools-native:do_populate_sysroot \
    virtual/kernel:do_deploy \
    ${IMAGE_BOOTLOADER}:do_deploy \
    rpi-config:do_deploy \
    ${@bb.utils.contains('MACHINE_FEATURES', 'armstub', 'armstubs:do_deploy', '' ,d)} \
    ${@bb.utils.contains('RPI_USE_U_BOOT', '1', 'u-boot:do_deploy', '',d)} \
    ${@bb.utils.contains('RPI_USE_U_BOOT', '1', 'u-boot-default-script:do_deploy', '',d)} \
"

do_deploy[recrdeps] = "do_build"

# SD card vfat partition image name
BOOTIMG_VFAT = "${IMAGE_NAME}.vfat"
BOOTIMG_LINK_VFAT = "${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.vfat"

def split_overlays(d, out, ver=None):
    dts = d.getVar("KERNEL_DEVICETREE")
    # Device Tree Overlays are assumed to be suffixed by '-overlay.dtb' (4.1.x) or by '.dtbo' (4.4.9+) string and will be put in a dedicated folder
    if out:
        overlays = oe.utils.str_filter_out('\S+\-overlay\.dtb$', dts, d)
        overlays = oe.utils.str_filter_out('\S+\.dtbo$', overlays, d)
    else:
        overlays = oe.utils.str_filter('\S+\-overlay\.dtb$', dts, d) + \
                   " " + oe.utils.str_filter('\S+\.dtbo$', dts, d)

    return overlays

do_deploy() {

    # Check if we are building with device tree support
    DTS="${KERNEL_DEVICETREE}"

    # Create a vfat image with boot files
    rm -f ${WORKDIR}/boot.img
    mkfs.vfat -F32 -n "boot" -S 512 -C ${WORKDIR}/boot.img 40960
    mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/bootfiles/* ::/ || bbfatal "mcopy cannot copy ${DEPLOY_DIR_IMAGE}/bootfiles/* into boot.img"
    if [ "${@bb.utils.contains("MACHINE_FEATURES", "armstub", "1", "0", d)}" = "1" ]; then
        mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/armstubs/${ARMSTUB} ::/ || bbfatal "mcopy cannot copy ${DEPLOY_DIR_IMAGE}/armstubs/${ARMSTUB} into boot.img"
    fi
    if test -n "${DTS}"; then
        # Copy board device trees to root folder
        for dtbf in ${@split_overlays(d, True)}; do
            dtb=`basename $dtbf`
            mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/$dtb ::$dtb || bbfatal "mcopy cannot copy ${DEPLOY_DIR_IMAGE}/$dtb into boot.img"
        done

        # Copy device tree overlays to dedicated folder
        mmd -i ${WORKDIR}/boot.img overlays
        for dtbf in ${@split_overlays(d, False)}; do
            dtb=`basename $dtbf`
            mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/$dtb ::overlays/$dtb || bbfatal "mcopy cannot copy ${DEPLOY_DIR_IMAGE}/$dtb into boot.img"
        done
    fi
    if [ "${RPI_USE_U_BOOT}" = "1" ]; then
        mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/u-boot.bin ::${BOOTIMG_KERNELIMAGE} || bbfatal "mcopy cannot copy ${DEPLOY_DIR_IMAGE}/u-boot.bin into boot.img"
        mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/boot.scr ::boot.scr || bbfatal "mcopy cannot copy ${DEPLOY_DIR_IMAGE}/boot.scr into boot.img"
        if [ ! -z "${INITRAMFS_IMAGE}" -a "${INITRAMFS_IMAGE_BUNDLE}" = "1" ]; then
            mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${INITRAMFS_LINK_NAME}.bin ::${KERNEL_IMAGETYPE} || bbfatal "mcopy cannot copy ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${INITRAMFS_LINK_NAME}.bin into boot.img"
        else
            mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} ::${KERNEL_IMAGETYPE} || bbfatal "mcopy cannot copy ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} into boot.img"
        fi
    else
        if [ ! -z "${INITRAMFS_IMAGE}" -a "${INITRAMFS_IMAGE_BUNDLE}" = "1" ]; then
            mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${INITRAMFS_LINK_NAME}.bin ::${BOOTIMG_KERNELIMAGE} || bbfatal "mcopy cannot copy ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${INITRAMFS_LINK_NAME}.bin into boot.img"
        else
            mcopy -v -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} ::${BOOTIMG_KERNELIMAGE} || bbfatal "mcopy cannot copy ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} into boot.img"
        fi
    fi

    # Add stamp file
    echo "${IMAGE_NAME}" > ${WORKDIR}/image-version-info
    mcopy -v -i ${WORKDIR}/boot.img ${WORKDIR}/image-version-info :: || bbfatal "mcopy cannot copy ${WORKDIR}/image-version-info into boot.img"

    # Deploy vfat partition
    cp ${WORKDIR}/boot.img ${DEPLOY_DIR_IMAGE}/${BOOTIMG_VFAT}
    ln -sf ${BOOTIMG_VFAT} ${BOOTIMG_LINK_VFAT}
    
}
addtask deploy
