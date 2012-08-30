#!/bin/bash
# author by Kun Wang 

usage="usage: run_analyze_apk.sh (start|stop) "
config_path=/home/wangkun/wk/AnalyzeApk/
run_path=/home/wangkun/wk/AnalyzeApk/
#run_path=/var/AnalyzeApk/
process_name=GetApkInfoServiceServer.jar
watcher_sh_name="$run_path"watcher_shell.sh

if [ $# -lt 1 ]; then
  echo $usage
  exit -1
fi

operation=$1

analyzeapk_server_stop() {
	kill -9 `ps aux | grep $process_name | grep -v grep | awk '{print $2}'`
	kill -9 `ps aux | grep $watcher_sh_name | grep -v grep | awk '{print $2}'`
  return 1
}

analyzeapk_server_start() {
		if [ `ps aux | grep $process_name | grep -v grep  | wc -l` -eq 1 ]
		then
			analyzeapk_server_stop
		else
			java -jar "$run_path"$process_name &
			sh ./$watcher_sh_name
			echo 'restart at' >> "$config_path"restart.log
			time=$(date +%F"-"%H"-"%M"-"%S)
			echo $time >> "$config_path"restart.log
		fi
  return 1
}




if [ $operation == "start" ]; then
  analyzeapk_server_start
elif [ $operation == "stop" ]; then
  analyzeapk_server_stop
else
  echo $usage
fi