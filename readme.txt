java -jar "$run_path"$process_name  $config_path &

java -jar GetApkInfoServiceServer.jar [config_path] &;  
#config_path 配置文件夹路径，包括：数据库配置，广告列表，脚本，临时文件夹；
#DBInfo.properties 数据库配置
#Ads.properties 广告列表
#defalut_icon.png 默认icon 0字节
#watcher_shell.sh 删除临时文件脚本及启动脚本
