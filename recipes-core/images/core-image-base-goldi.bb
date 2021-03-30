SUMMARY = "A console-only image that fully supports the target device \
hardware."

IMAGE_NAME = "${@ "image-CU" if bb.utils.to_boolean(d.getVar('CONTROLUNIT')) else "image-PS" }"
IMAGE_LINK_NAME = "${@ "image-CU-${MACHINE}" if bb.utils.to_boolean(d.getVar('CONTROLUNIT')) else "image-PS-${MACHINE}" }"

IMAGE_FEATURES += "splash"

LICENSE = "MIT"

inherit core-image

WKS_FILE = "sdimage-goldi.wks"

IMAGE_INSTALL_append = " slotraucs"
do_image_wic[depends] += " boot-partition-image:do_deploy"
