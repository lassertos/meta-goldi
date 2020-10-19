SUMMARY = "Provides the GOLDi Services"
HOMEPAGE = "https://github.com/lassertos/GOLDi-Services"
SRCREV = "${AUTOREV}"
SRC_URI = "git://git@github.com/lassertos/GOLDi-Services.git;protocol=ssh"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
SRC_URI[sha256sum] = "75914cc6dc4300916d48e284ce6b10d5c211833d6d0752e3ac2d02bf6c0cdffd"
S = "${WORKDIR}/git"

DEPENDS = "bcm2835 cjson libwebsockets systemd"

inherit autotools
