SUMMARY = "Provides an lightweight updater for rauc"
HOMEPAGE = ""
LICENSE = "MIT"

SRC_URI = ""
SRC_URI[md5sum] = ""
SRC_URI[sha256sum] = ""

S = "${WORKDIR}/${PN}-${PV}"

RAUC_BUNDLE_BUILD = "${STARTTIME}"

do_install() {
    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/goldi-rauc-updater.sh ${D}${bindir}/goldi-rauc-updater.sh
}

FILES_${PN} += "${bindir}/goldi-rauc-updater.sh"
