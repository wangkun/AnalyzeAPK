#!/bin/bash
# author by Kun Wang

usage="usage: run_scan_apk.sh  "
config_path=/home/wangkun/wk/security/
run_path=/home/wangkun/wk/security/
process_name=uploadToFTP.jar

apk_numbers_per_time=500 

while [ true ]
	do
		echo "apk_numbers_per_time="$apk_numbers_per_time
		java -jar "$run_path"$process_name $apk_numbers_per_time
		echo "scaning once sleep a while 10sec"
		time=$(date +%F" "%H":"%M":"%S)
		echo $time
		sleep 10
	done
	