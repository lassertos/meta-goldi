SUMMARY = "Provides an lightweight updater for rauc"
HOMEPAGE = ""
LICENSE = "MIT"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append = "file://goldi-rauc-updater.sh"

S = "${WORKDIR}/${PN}-${PV}"

do_install() {
    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/goldi-rauc-updater.sh ${D}${bindir}/goldi-rauc-updater.sh
}

FILES_${PN} += "${bindir}/goldi-rauc-updater.sh"
