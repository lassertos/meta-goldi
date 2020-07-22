inherit bundle

RAUC_BUNDLE_COMPATIBLE = "RPI4"
RAUC_BUNDLE_SLOTS = "rootfs"
RAUC_SLOT_rootfs = "core-image-minimal"
RAUC_BUNDLE_BUILD = "${STARTTIME}"

RAUC_KEY_FILE = "${THISDIR}/files/ca.key.pem"
RAUC_CERT_FILE = "${THISDIR}/files/ca.cert.pem"
