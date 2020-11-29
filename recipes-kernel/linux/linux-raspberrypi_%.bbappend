CMDLINE_remove = "root=/dev/mmcblk0p2"

do_configure_prepend() {
    config_setup

    mv -f ${B}/.config ${B}/.config.patched
    CONF_SED_SCRIPT=""
    
    kernel_configure_variable SQUASHFS y
    kernel_configure_variable USB_G_WEBCAM y
    kernel_configure_variable MEDIA_SUPPORT y
    kernel_configure_variable USB_CONFIGFS_F_UVC y
    kernel_configure_variable USB_F_UVC y
    kernel_configure_variable USB_VIDEO_CLASS y
    kernel_configure_variable VIDEO_V4L2 y
    kernel_configure_variable VIDEOBUF2_V4L2 y

    # Keep this the last line
    # Remove all modified configs and add the rest to .config
    sed -e "${CONF_SED_SCRIPT}" < '${B}/.config.patched' >> '${B}/.config'
    rm -f ${B}/.config.patched
}
