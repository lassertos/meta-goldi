CMDLINE_remove = "root=/dev/mmcblk0p2"

do_configure_prepend() {
    config_setup

    mv -f ${B}/.config ${B}/.config.patched
    CONF_SED_SCRIPT=""
    
    kernel_configure_variable SQUASHFS y

    # Keep this the last line
    # Remove all modified configs and add the rest to .config
    sed -e "${CONF_SED_SCRIPT}" < '${B}/.config.patched' >> '${B}/.config'
    rm -f ${B}/.config.patched
}
