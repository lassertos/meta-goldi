FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append := " \
	file://system.conf \
	file://post-install.sh \
	file://ca.cert.pem \
"

do_install_append () {
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/rauc
    install -d ${D}${libdir}
    install -d ${D}${libdir}/rauc
    install -m 755 ${WORKDIR}/post-install.sh ${D}${libdir}/rauc
    install -m 755 ${WORKDIR}/ca.cert.pem ${D}${sysconfdir}/rauc
}
