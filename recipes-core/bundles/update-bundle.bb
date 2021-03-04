inherit bundle

RAUC_BUNDLE_COMPATIBLE = "${MACHINE}"
RAUC_BUNDLE_SLOTS = "rootfs bootloader"
RAUC_SLOT_rootfs = "core-image-minimal"
RAUC_SLOT_bootloader = "core-image-minimal"
RAUC_SLOT_bootloader[file] = "boot-partition-image-${MACHINE}.img"
RAUC_BUNDLE_BUILD = "${COMMITNR}"

RAUC_KEY_FILE = "${THISDIR}/files/ca.key.pem"
RAUC_CERT_FILE = "${THISDIR}/files/ca.cert.pem"
RAUC_KEYRING_FILE = "${THISDIR}/files/ca.cert.pem"

RAUC_CASYNC_BUNDLE = "1"

BUNDLE_NAME = "${@ "update-bundle-CU-${MACHINE}" if bb.utils.to_boolean(d.getVar('CONTROLUNIT')) else "update-bundle-PS-${MACHINE}" }"
