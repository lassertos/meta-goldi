#!/bin/sh
fw_setenv BOOT_ORDER "$(fw_printenv BOOT_ORDER | sed -e s/BOOT_ORDER=//g | awk '{print $2,$1}')"
