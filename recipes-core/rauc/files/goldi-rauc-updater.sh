echo updater started at $(date)
TIMESTAMP=$(stat -c %y update-info)
wget -O /etc/rauc/update-info http://192.168.179.24/updates/update-info
NEW_VERSION=$(cat /etc/rauc/update-info | grep ^Version: | cut -d: -f2 | sed "s/'//g" | tr -d [:space:])
NEW_BUILD=$(cat /etc/rauc/update-info | grep ^Build: | cut -d: -f2 | sed "s/'//g" | tr -d [:space:])
echo new version=$NEW_VERSION
echo new build=$NEW_BUILD
BOOTED_VERSION=$(rauc status --detailed --output-format=json | jq '.slots | .[] | to_entries[] | select(.value.state == "booted") | .value.slot_status.bundle.version' | sed 's/"//g')
BOOTED_BUILD=$(rauc status --detailed --output-format=json | jq '.slots | .[] | to_entries[] | select(.value.state == "booted") | .value.slot_status.bundle.build' | sed 's/"//g')
echo booted version=$BOOTED_VERSION
echo booted build=$BOOTED_BUILD
INACTIVE_VERSION=$(rauc status --detailed --output-format=json | jq '.slots | .[] | to_entries[] | select(.value.state == "inactive") | .value.slot_status.bundle.version' | sed 's/"//g')
INACTIVE_BUILD=$(rauc status --detailed --output-format=json | jq '.slots | .[] | to_entries[] | select(.value.state == "inactive") | .value.slot_status.bundle.build' | sed 's/"//g')
echo inactive version=$INACTIVE_VERSION
echo inactive build=$INACTIVE_BUILD
if [ "$NEW_VERSION" != "$BOOTED_VERSION" ] || [ "$NEW_BUILD" != "$BOOTED_BUILD" ]
then
	if [ "$NEW_VERSION" = "$INACTIVE_VERSION" ] && [ "$NEW_BUILD" = "$INACTIVE_BUILD" ]
	then
		echo changing boot order
		fw_setenv BOOT_ORDER "$(fw_printenv BOOT_ORDER | cut -d= -f2 | awk '{print $2,$1}')"
		shutdown -r 1
	else
		echo installing update
		wget -O /etc/rauc/update.raucb http://192.168.179.24/updates/update.raucb
		rauc install /etc/rauc/update.raucb
		rm /etc/rauc/update.raucb
	fi
else
	echo no update needed
fi
