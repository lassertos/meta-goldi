#!/bin/sh
fw_setenv BOOT_ORDER $RAUC_CURRENT_BOOTNAME && sync -d /boot/uboot.env
