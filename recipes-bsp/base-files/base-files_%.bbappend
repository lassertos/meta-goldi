do_install_append () {
    cat >> ${D}${sysconfdir}/fstab <<EOF

# Needed for Booting after update
/dev/mmcblk0p1	/boot	vfat	defaults	0	0

EOF
}
