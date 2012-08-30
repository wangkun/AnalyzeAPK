# !/bin/sh
# author by Kun Wang 
run_path=/home/wangkun/wk/AnalyzeApk/
# run_path=/var/AnalyzeApk/
config_path=/home/wangkun/wk/AnalyzeApk/

MAX_CPU=400

mkdir "$run_path"deFiles
mkdir "$run_path"apks

process_name=GetApkInfoServiceServer.jar
one=1

restart_process(){
			rm -rf "$config_path"/deFiles/*
			rm -rf "$config_path"/apks/*.apk
			kill -9 `ps aux | grep $process_name | grep -v grep | awk '{print $2}'`
			java -jar "$run_path"$process_name $config_path &
			sendemail -f wangkun@jike.com -t wangkun@jike.com -s smtp.jike.com -u "restart process" -xu wangkun@jike.com -xp jikehbbd -o message-charset=utf-8 -m "restart process once for CPU use"
			echo 'restart at' >> "$config_path"/restart.log
			time=$(date +%F" "%H":"%M":"%S)
			echo $time >> "$config_path"/restart.log
			return 1;
}

while [ true ]
	do
		if [ `ps aux | grep $process_name | grep -v grep  | wc -l` -eq 1 ]
		then
			sleep 120
			echo 'sleep 120 sec'
			#delete temp files; find "$config_path"deFiles/ -name '*_*' -cmin +5 | xargs rm -rf
			find "$config_path"deFiles/ -name '*_*' -cmin +2 | xargs rm -rf
			find "$config_path"apks/ -name '*.apk' -cmin +2 | xargs rm -rf
			#delete logs,nohup.out; if > 1G;
			if [ `find "$run_path" -size +500000000c | wc -l` -ge 1 ]
				then 
					#find "$run_path" -size +500000000c | xargs rm
					> "$run_path"nohup.out
					> "$run_path"Jike.AnalyzeAPK.log
					sendemail -f wangkun@jike.com -t wangkun@jike.com -s smtp.jike.com -u "rm ge 500M log file" -xu wangkun@jike.com -xp jikehbbd -o message-charset=utf-8 -m "rm ge 500M log file"
			fi 
			pid=`ps -e|egrep $process_name | awk '{print $1}'` #获取进程id
			cpu=`top -n 1 -p $pid|tail -2|head -1|awk '{ssd=NF-4} {print $ssd}'`
			if [ ${cpu} -gt ${MAX_CPU} ] 
			then
				sleep 5
				if [ ${cpu} -eq `top -n 1 -p $pid|tail -2|head -1|awk '{ssd=NF-4} {print $ssd}'` ]
				then
					restart_process
				fi
			fi 
		else
			
			
			rm -rf "$config_path"/deFiles/*
			rm -rf "$config_path"/apks/*.apk
			kill -9 `ps aux | grep $process_name | grep -v grep | awk '{print $2}'`
			java -jar "$run_path"$process_name $config_path &
			sendemail -f wangkun@jike.com -t wangkun@jike.com -s smtp.jike.com -u "restart process" -xu wangkun@jike.com -xp jikehbbd -o message-charset=utf-8 -m "restart process once"
			echo 'restart at' >> "$config_path"/restart.log
			time=$(date +%F" "%H":"%M":"%S)
			echo $time >> "$config_path"/restart.log
			
		fi

		time=$(date +%F" "%H":"%M":"%S)
		echo $time
	done
