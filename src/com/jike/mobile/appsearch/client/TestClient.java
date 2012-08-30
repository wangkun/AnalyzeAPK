package com.jike.mobile.appsearch.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.LinkedBlockingQueue;

public class TestClient {

    private static String apkPath = "366089823295877054";
    private static String apk_key = "fail2720.csv";//"failedFileKey.log";
    
    public final static LinkedBlockingQueue<String> decodedApps = new LinkedBlockingQueue<String>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        // 366089823295877054
//        GetApkInfosClient.getAPKinfo(apkPath );
        decodedApps.clear();
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(  
                    new FileInputStream(apk_key)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
  
        try {
            for (String key = br.readLine(); key != null; key = br.readLine()) {  
                decodedApps.add(key); 
//                decodedApps.add(key); 
                if (decodedApps.size()>20) {
                    break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
        try {
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
        int i = 2;
        while (i > 0) {
            Thread t1 = new AnalyzeThread();
            t1.start();
            i--;
        }
        
//        Runnable runnable = new Runnable() {
//            public void run() {
//                while (decodedApps.size()>0) {
//                    String key = null;
//                    try {
//                        key =  decodedApps.take();
//                        System.out.println(key + " @ size = "+decodedApps.size());
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    GetApkInfosClient.getAPKinfo(key);
//                }
//            }
//        };
//        Thread t1 = new Thread(runnable);
//        t1.run();
        

    }
    
    

}
