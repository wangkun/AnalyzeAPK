package com.jike.mobile.appsearch.client;

public class AnalyzeThread extends Thread {
    @Override
    public void run() {
        while(!Thread.interrupted()){
            String key = null;
            try {
                key =  TestClient.decodedApps.take();
                System.out.println(key + " @ size = "+TestClient.decodedApps.size());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            GetApkInfosClient.getAPKinfo(key);
            
        }
        super.run();
    }

}
