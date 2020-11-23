DESCRIPTION = "Lib(X)SVF: A library for implementing SVF and XSVF JTAG players"
SECTION = "base"
HOMEPAGE = "http://www.clifford.at/libxsvf/"
AUTHOR = "Clifford Wolf"

LICENSE = "ISC"
SRCREV = "104"
S = "${WORKDIR}/trunk"
LIC_FILES_CHKSUM = "file://COPYING;md5=8394f7c37610175a9945c2e36a88ffb9"

EXTRA_OEMAKE = "'CC=${CC}' 'RANLIB=${RANLIB}' 'AR=${AR}' 'CFLAGS=${CFLAGS} -I${S}/include' 'BUILDDIR=${S}' 'DESTDIR=${D}'"

COMPATIBLE_MACHINE = "^rpi$"

SRC_URI = "svn://svn.clifford.at/libxsvf/;module=trunk;protocol=http"

do_compile() {
        oe_runmake libxsvf.a 'CC=${CC}'
}

do_install() {
	install -d ${D}${includedir}
	install -d ${D}${libdir}
	install -m 755 ${S}/libxsvf.h ${D}${includedir}
	install -m 755 ${S}/libxsvf.a ${D}${libdir}
}

addtask do_compile
addtask do_install after do_compile

FILES_${PN} = ""
