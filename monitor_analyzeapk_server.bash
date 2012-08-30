#!/bin/bash
# author wangkun@Jike.com

if [ `ps aux | grep "GetApkInfoServiceServer.jar" | grep -v grep  | wc -l` -ne 1 ];then
 echo "GetApkInfoServiceServer is down!"
 exit 2
fi 
echo "I am OK"
exit 0
