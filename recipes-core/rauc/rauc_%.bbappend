FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append := " \
	file://system.conf \
	file://pre-install.sh \
	file://post-install.sh \
	file://ca.cert.pem \
	file://goldi-rauc-updater.sh \
"

do_install_append () {
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/rauc
    install -d ${D}${libdir}
    install -d ${D}${libdir}/rauc
    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/post-install.sh ${D}${libdir}/rauc
    install -m 755 ${WORKDIR}/pre-install.sh ${D}${libdir}/rauc
    install -m 755 ${WORKDIR}/ca.cert.pem ${D}${sysconfdir}/rauc
    install -m 755 ${WORKDIR}/goldi-rauc-updater.sh ${D}${bindir}/goldi-rauc-updater.sh
}
