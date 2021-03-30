WKS_FILE = "sdimage-goldi.wks"
do_image_wic[depends] += " boot-partition-image:do_deploy"
