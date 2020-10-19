SUMMARY = "Provides the GOLDi Services"
HOMEPAGE = "https://github.com/lassertos/goldi-svf-player"
SRC_URI = "http://192.168.179.24/updates/goldiservices-1.0.tar.gz"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
SRC_URI[sha256sum] = "693a1d1a4dec26bca7c538ea3d9b54f90cfeca1a757708822724a602e8df74fd"

DEPENDS = "bcm2835 cjson libwebsockets systemd"

inherit autotools
