FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-rpi-3-and-4-env.patch \
            file://0002-mmc-changes.patch \
            file://fw_env.config \
            "

do_install_append () {
    install -d ${D}${sysconfdir}
    install -m 644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/
}
