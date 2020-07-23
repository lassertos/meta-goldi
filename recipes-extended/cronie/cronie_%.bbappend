FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append := " \
	file://crontab-goldi \
"

do_install_append () {
    install -m 0755 ${WORKDIR}/crontab-goldi ${D}${sysconfdir}/crontab
    chmod 600 ${D}${sysconfdir}/crontab
}
