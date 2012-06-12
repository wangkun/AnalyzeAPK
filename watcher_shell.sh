# !/bin/sh
# author by Kun Wang 
# run_path=/home/wangkun/wk/AnalyzeApk/
run_path=/var/AnalyzeApk/

mkdir "$run_path"deFiles
mkdir "$run_path"apks

process_name=GetApkInfoServiceServer.jar
one=1
while [ true ]
	do
		if [ `ps aux | grep $process_name | grep -v grep  | wc -l` -eq 1 ]
		then
			sleep 120
			echo 'sleep 120 sec'
			# find "$run_path"deFiles/ -name '*_*' -cmin +5 | xargs rm -rf
			find "$run_path"deFiles/ -name '*_*' -cmin +2 | xargs rm -rf
			find "$run_path"apks/ -name '*.apk' -cmin +2 | xargs rm -rf
		else
			
			rm -rf "$run_path"/deFiles/*
			rm -rf "$run_path"/apks/*.apk
			kill -9 `ps aux | grep $process_name | grep -v grep | awk '{print $2}'`
			java -jar "$run_path"$process_name &
			echo 'restart at' >> "$run_path"/restart.log
			time=$(date +%F"-"%H"-"%M"-"%S)
			echo $time >> "$run_path"/restart.log
			
		fi

		time=$(date +%F"-"%H"-"%M"-"%S)
		echo $time
	done
