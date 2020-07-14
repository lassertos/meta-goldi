FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append := " file://fw_env.config file://uboot_goldi_bootargs_defconfig"

do_install_append () {
    install -m 644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/
}
