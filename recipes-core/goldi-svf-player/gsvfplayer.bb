SUMMARY = "Provides the goldi-svf-player"
HOMEPAGE = "https://github.com/lassertos/goldi-svf-player"
SRC_URI = "http://192.168.179.24/updates/gsvfplayer-1.0.tar.gz"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
SRC_URI[sha256sum] = "c8d7cb2d699a95ed2aaa908ade871460419c9efac435f26eed171f2a10f37459"

DEPENDS = "bcm2835"

inherit autotools
