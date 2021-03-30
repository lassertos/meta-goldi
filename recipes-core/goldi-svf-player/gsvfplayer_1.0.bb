SUMMARY = "Provides the goldi-svf-player"
HOMEPAGE = "https://github.com/lassertos/goldi-svf-player"
SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/lassertos/goldi-svf-player.git;protocol=http"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
S = "${WORKDIR}/git"

DEPENDS = "bcm2835"

inherit autotools
