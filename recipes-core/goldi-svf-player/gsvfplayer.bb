SUMMARY = "Provides the goldi-svf-player"
HOMEPAGE = "https://github.com/lassertos/goldi-svf-player"
SRC_URI = "http://192.168.179.24/updates/gsvfplayer-1.0.tar.gz"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
SRC_URI[sha256sum] = "c7448763d09c286bff6b14cdcc2425e6556fd66e81f8f4257dc20fe13be4a28d"

DEPENDS = "bcm2835"

inherit autotools
