package com.jike.mobile.appsearch.security;


import com.jike.mobile.appsearch.datebase.ApkInfoBuilder;
import com.jike.mobile.appsearch.datebase.SecurityBuilder;
import com.jike.mobile.appsearch.util.CommonUtils;
import com.jike.mobile.appsearch.util.GetApkFileFromCassandra;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

public class ScanApk {
    private static final Logger log = LogManager
            .getLogger(ScanApk.class);
    static ArrayList<String> apkList = new ArrayList<String>();
    static final String APK_PATH = "./apks/";
    static int apk_numbers = 500;
    static final int SCANNING = 1111;
    static final int SECURITY = 1;
    static final int VIRUS = -1;
    
    static final String  FTP_DIR = "/home/ftp2/admin/file/";
    

    /**
     * @param args
     */
    public static void main(String[] args) {
        File tempDeFiles=new File(APK_PATH);
        if (!tempDeFiles.exists()||!tempDeFiles.isDirectory()) {
            tempDeFiles.mkdir();
        }
        if (args.length==1) {
            apk_numbers = Integer.parseInt(args[0]);
        }
        
        
        apkList = ApkInfoBuilder.getApkListBySecurityLevel(0, apk_numbers);
//        TODO:
//        while (true) {
            
            
            ApkInfoBuilder.changeApkSecurityLevel(apkList, SCANNING);
            
            log.debug("size=" + apkList.size());
            if (apkList.size() < apk_numbers/2) {
                return;
            }
            String apkFTPDir=getApksFormCassandraAndUploadToFTP(apkList);
            int STaskReqID=SecurityBuilder.beginScanning(apkFTPDir);
            int count = 0;
        do {
            try {
                Thread.sleep(10*1000);
                log.info("waiting for scanning: " + (++count) * 10 + " secends ……");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } while (0 == SecurityBuilder.IsScanFinished(STaskReqID));
            if (1 == SecurityBuilder.IsScanFinished(STaskReqID)) {
                log.info("scan finished");
                ApkInfoBuilder.changeApkSecurityLevel(apkList, SECURITY);
            }else {
                log.error("Scan Failed");
            }
            ArrayList<String> virusApkList = SecurityBuilder.getVirusApkList(STaskReqID);
            log.info("virusApkList.size()="+virusApkList.size());
            log.info("virusApkList : \n"+virusApkList);
            ApkInfoBuilder.changeApkSecurityLevel(virusApkList, VIRUS);
            deleteFTPDir(FTP_DIR+apkFTPDir);
            clearLocalDir(APK_PATH+apkFTPDir);
            log.info("finished once apk_numbers=" + apk_numbers);
            
            
//            apkList = ApkInfoBuilder.getApkListBySecurityLevel(0, apk_numbers);
//            if (apkList.size()<=100) {
//                try {
//                    Thread.sleep(10*60*1000);
//                    log.info("sleep 10 mins ,because apksize is not enough ");
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
        
    }
    
    /**
     * 
     * @param apkList
     * @param apksPath
     */
    public static String getApksFormCassandraAndUploadToFTP(ArrayList<String> apkList) {
        
        String apkFileName="apk_"+System.currentTimeMillis();
        for (int i = 0; i < apkList.size(); i++) {
            GetApkFileFromCassandra.getAPKtoFTP(apkList.get(i), APK_PATH+apkFileName);
            log.debug("upload completed numbers = " + i);
        }
        FTP ftp=new FTP("58.68.224.155",21,"admin","root@wanAMAAS");
        ftp.ftpLogin();
        ftp.uploadDirectory(APK_PATH+apkFileName, FTP_DIR);
        log.debug("ftp path = "+ FTP_DIR+apkFileName+"/");
        ftp.ftpLogOut(); 
        return apkFileName;
    }
    
    
    public static void deleteFTPDir(String dir){
        FTP ftp=new FTP("58.68.224.155",21,"admin","root@wanAMAAS");
        ftp.ftpLogin();
        ftp.deleteDir(dir);
        ftp.ftpLogOut(); 
    }
    public static void clearLocalDir(String string) {
        CommonUtils.deleteFiles(new File(string));
    }
    

}
