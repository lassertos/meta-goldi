SUMMARY = "Provides the GOLDi Services"
HOMEPAGE = "https://github.com/lassertos/GOLDi-Services"
SRCREV = "${AUTOREV}"
SRC_URI = "git://git@github.com/lassertos/GOLDi-Services.git;protocol=ssh;branch=development"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
SRC_URI[sha256sum] = "75914cc6dc4300916d48e284ce6b10d5c211833d6d0752e3ac2d02bf6c0cdffd"
S = "${WORKDIR}/git"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append := " \
	file://goldi-rauc-updater.sh \
	file://goldi-rauc-updater.service \
	file://goldi-rauc-updater.timer \
"

TARGET_CFLAGS += "-O0 -g"
EXTRA_OECONF_append = " ${@ "--enable-cu" if bb.utils.to_boolean(d.getVar('CONTROLUNIT')) else "" }"

inherit autotools systemd pkgconfig

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN}_append = " goldi-rauc-updater.timer ${@ " GOLDiCommunicationService.service GOLDiProgrammingService.socket GOLDiCommandService.socket" if bb.utils.to_boolean(d.getVar('CONTROLUNIT')) else " GOLDiCommunicationService.service GOLDiProgrammingService.socket GOLDiInitializationService.socket GOLDiWebcamService.socket GOLDiProtectionService.socket" }"

do_compile() {
	CFLAGS="${CFLAGS} `pkg-config --cflags gstreamer-1.0 gstreamer-plugins-base-1.0`"
	LDLIBS="${LDFLAGS} `pkg-config --libs gstreamer-1.0 gstreamer-plugins-base-1.0`"
	oe_runmake -e
}

do_install_append() {
	[ -d "${DEPLOY_DIR_IMAGE}/data" ] && rm -r ${DEPLOY_DIR_IMAGE}/data
	install -d 0644 ${DEPLOY_DIR_IMAGE}/data
	install -d 0644 ${DEPLOY_DIR_IMAGE}/data/GOLDiServices
	install -d ${D}${systemd_system_unitdir}
    	install -d ${D}${bindir}
  	install -m 0644 ${S}/src/systemd-files/GOLDiCommunicationService.service ${D}${systemd_system_unitdir}
  	install -m 0644 ${S}/src/systemd-files/GOLDiProgrammingService.service ${D}${systemd_system_unitdir}
  	install -m 0644 ${S}/src/systemd-files/GOLDiProgrammingService.socket ${D}${systemd_system_unitdir}
    	install -m 755 ${WORKDIR}/goldi-rauc-updater.sh ${D}${bindir}/goldi-rauc-updater.sh
    	install -m 0644 ${WORKDIR}/goldi-rauc-updater.service ${D}${systemd_system_unitdir}
    	install -m 0644 ${WORKDIR}/goldi-rauc-updater.timer ${D}${systemd_system_unitdir}
  	if [ "${@d.getVar('CONTROLUNIT')}" = "1" ] 
  		then
  			sed -i 's/@@UNITTYPE@@/CU/' ${D}${bindir}/goldi-rauc-updater.sh
	  		cp ${S}/src/templates/DeviceDataCUTemplate.json ${DEPLOY_DIR_IMAGE}/data/GOLDiServices/DeviceData.json
	  		install -m 0644 ${S}/src/systemd-files/GOLDiCommandService.service ${D}${systemd_system_unitdir}
	  		install -m 0644 ${S}/src/systemd-files/GOLDiCommandService.socket ${D}${systemd_system_unitdir}
	  	else
	  		sed -i 's/@@UNITTYPE@@/PS/' ${D}${bindir}/goldi-rauc-updater.sh
			cp ${S}/src/templates/DeviceDataPSTemplate.json ${DEPLOY_DIR_IMAGE}/data/GOLDiServices/DeviceData.json
	  		install -m 0644 ${S}/src/systemd-files/GOLDiProtectionService.service ${D}${systemd_system_unitdir}
	  		install -m 0644 ${S}/src/systemd-files/GOLDiWebcamService.service ${D}${systemd_system_unitdir}
	  		install -m 0644 ${S}/src/systemd-files/GOLDiInitializationService.service ${D}${systemd_system_unitdir}
	  		install -m 0644 ${S}/src/systemd-files/GOLDiProtectionService.socket ${D}${systemd_system_unitdir}
	  		install -m 0644 ${S}/src/systemd-files/GOLDiWebcamService.socket ${D}${systemd_system_unitdir}
	  		install -m 0644 ${S}/src/systemd-files/GOLDiInitializationService.socket ${D}${systemd_system_unitdir}
  	fi
    	sed -i 's/@@MACHINE@@/${MACHINE}/' ${D}${bindir}/goldi-rauc-updater.sh
}

DEPENDS = "bcm2835 cjson libwebsockets systemd libxsvf gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-good glib-2.0"

FILES_${PN} += " /data /data/GOLDiServices /data/GOLDiServices/DeviceData.json ${systemd_system_unitdir}/goldi-rauc-updater.service"
