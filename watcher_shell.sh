# !/bin/sh
# author by Kun Wang 

process_name=GetApkInfoServiceServer.jar
one=1
while [ true ]
	do
		if [ `ps aux | grep $process_name | grep -v grep  | wc -l` -eq 1 ]
		then
			sleep 10
			echo 'sleep 10 sec'
		else
			kill -9 `ps aux | grep $process_name | grep -v grep | awk '{print $2}'`
			java -jar $process_name &
			echo 'restart'
		fi

		time=$(date +%F"-"%H"-"%M"-"%S)
		echo $time
	done