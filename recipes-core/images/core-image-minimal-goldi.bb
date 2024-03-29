SUMMARY = "The image used for flashing the sd-card."

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = " "

IMAGE_NAME = "${@ "image-CU" if bb.utils.to_boolean(d.getVar('CONTROLUNIT')) else "image-PS" }"
IMAGE_LINK_NAME = "${@ "image-CU-${MACHINE}" if bb.utils.to_boolean(d.getVar('CONTROLUNIT')) else "image-PS-${MACHINE}" }"

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE ?= "8192"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

WKS_FILE = "sdimage-goldi.wks"

IMAGE_INSTALL_append = " slotraucs"
do_image_wic[depends] += " boot-partition-image:do_deploy"
