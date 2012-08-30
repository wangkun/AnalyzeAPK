package com.jike.mobile.appsearch.util;

import com.jike.mobile.appsearch.datebase.ApkInfoBuilder;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

public class UpdatetimePatch {

    private static final Logger log = LogManager.getLogger(UpdatetimePatch.class);
    
    public final static LinkedBlockingQueue<String> TODO_APPS_QUEUE = new LinkedBlockingQueue<String>();
    
    
    public static void main(String[] args) {

        boolean re=ApkInfoBuilder.findUpdatetimePatch();
        if (re) {
            log.debug("TODO_APPS_QUEUE.size()="+TODO_APPS_QUEUE.size());
        }
        while(TODO_APPS_QUEUE.size()>0){
            String keyString = "";
            try {
                keyString = TODO_APPS_QUEUE.take();
                
                log.debug("TODO_APPS_QUEUE.size()="+TODO_APPS_QUEUE.size() + ", keyString=" + keyString);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (keyString.length()>1&&keyString.length()<20) {
                File apkFile = GetApkFileFromCassandra.getAPK(keyString,Constants.APKS_PATH);
                if (apkFile!=null&&apkFile.exists()) {
                    ApkInfoBuilder.updatetimePatch(keyString, ""+CommonUtils.getApkMakeTime(apkFile.getAbsolutePath()));
                    CommonUtils.forceDelete(apkFile);
                }else {
                    log.error("!apkFile.exists()");
                }
                
            }
            
        }
    }

}
