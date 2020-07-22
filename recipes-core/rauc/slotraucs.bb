SUMMARY = "Provides a slot.raucs file for the sd-card image"
HOMEPAGE = ""
LICENSE = "MIT"

#LIC_FILES_CHKSUM = "file://COPYING;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = ""
SRC_URI[md5sum] = ""
SRC_URI[sha256sum] = ""

S = "${WORKDIR}/${PN}-${PV}"

RAUC_BUNDLE_BUILD = "${STARTTIME}"

do_install() {
    install -d ${D}/
    install -m 755 ${WORKDIR}/slot.raucs ${D}/slot.raucs
}
addtask install 

python do_writeslotraucs() {
    slot_raucs_path = d.expand('${WORKDIR}')
    bb.utils.mkdirhier(slot_raucs_path)
    try:
        slotraucs = open('%s/slot.raucs' % slot_raucs_path, 'w')
    except OSError:
        raise bb.build.FuncFailed('Unable to open slot.raucs')
    
    slotraucs.write('[slot]\n')
    slotraucs.write(d.expand('bundle.compatible=${RAUC_BUNDLE_COMPATIBLE}\n'))
    slotraucs.write(d.expand('bundle.version=${RAUC_BUNDLE_VERSION}\n'))
    slotraucs.write(d.expand('bundle.description=${RAUC_BUNDLE_DESCRIPTION}\n'))
    slotraucs.write(d.expand('bundle.build=${RAUC_BUNDLE_BUILD}'))
    
    slotraucs.close()
}
addtask writeslotraucs before do_install

WKS_FILE = "sdimage-goldi.wks"

FILES_${PN} += "/slot.raucs"
