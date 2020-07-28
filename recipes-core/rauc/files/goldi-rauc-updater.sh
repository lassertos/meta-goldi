echo updater started at $(date)

#names may be changed for final environment
UPDATE_SERVER="http://192.168.179.24/updates"
BOOT_FILES="bcm2835-bootfiles"
UPDATE_INFO="update-info"
UPDATE_FILE="update.raucb"
RAUC_DIR="/etc/rauc"

#download update-info and collect necessary information
wget -O ${RAUC_DIR}/${UPDATE_INFO} ${UPDATE_SERVER}/${UPDATE_INFO} || { echo could not retrieve ${UPDATE_INFO} && exit 1; }
BOOTLOADER_UPDATE=$(cat ${RAUC_DIR}/${UPDATE_INFO} | grep ^bootloader-update | cut -d: -f2)
NEW_VERSION=$(cat ${RAUC_DIR}/${UPDATE_INFO} | grep ^Version: | cut -d: -f2 | sed "s/'//g" | tr -d [:space:])
NEW_BUILD=$(cat ${RAUC_DIR}/${UPDATE_INFO} | grep ^Build: | cut -d: -f2 | sed "s/'//g" | tr -d [:space:])
BOOTED_VERSION=$(rauc status --detailed --output-format=json | jq '.slots | .[] | to_entries[] | select(.value.state == "booted") | .value.slot_status.bundle.version' | sed 's/"//g')
BOOTED_BUILD=$(rauc status --detailed --output-format=json | jq '.slots | .[] | to_entries[] | select(.value.state == "booted") | .value.slot_status.bundle.build' | sed 's/"//g')
INACTIVE_VERSION=$(rauc status --detailed --output-format=json | jq '.slots | .[] | to_entries[] | select(.value.state == "inactive") | .value.slot_status.bundle.version' | sed 's/"//g')
INACTIVE_BUILD=$(rauc status --detailed --output-format=json | jq '.slots | .[] | to_entries[] | select(.value.state == "inactive") | .value.slot_status.bundle.build' | sed 's/"//g')

#output for log
echo new version=$NEW_VERSION
echo new build=$NEW_BUILD
echo booted version=$BOOTED_VERSION
echo booted build=$BOOTED_BUILD
echo inactive version=$INACTIVE_VERSION
echo inactive build=$INACTIVE_BUILD
echo bootloader updated=$BOOTLOADER_UPDATE

#check if update is already installed on booted partition
if [ "$NEW_VERSION" != "$BOOTED_VERSION" ] || [ "$NEW_BUILD" != "$BOOTED_BUILD" ]
then
	#check if update is already installed on inactive partition
	if [ "$NEW_VERSION" = "$INACTIVE_VERSION" ] && [ "$NEW_BUILD" = "$INACTIVE_BUILD" ]
	then
		echo changing boot order
		fw_setenv BOOT_ORDER "$(fw_printenv BOOT_ORDER | cut -d= -f2 | awk '{print $2,$1}')"
		shutdown -r 1
	else
		echo downloading update
		[ "$BOOTLOADER_UPDATE" = "1" ] && wget -r -np -nH --cut-dirs=1 -R "index.html?*" -P ${RAUC_DIR} ${UPDATE_SERVER}/${BOOT_FILES}/ || { echo could not retrieve ${BOOT_FILES} && exit 1; }
		wget -O ${RAUC_DIR}/${UPDATE_FILE} ${UPDATE_SERVER}/${UPDATE_FILE} || { echo could not retrieve ${UPDATE_FILE} && exit 1; }
		echo installing update
		[ "$BOOTLOADER_UPDATE" = "1" ] && echo installing new bootloader && cp -r ${RAUC_DIR}/${BOOT_FILES}/* /boot && rm -r ${RAUC_DIR}/${BOOT_FILES}
		echo installing new rootfs
		rauc install ${RAUC_DIR}/${UPDATE_FILE} && rm ${RAUC_DIR}/${UPDATE_FILE}
	fi
else
	echo no update needed
fi
