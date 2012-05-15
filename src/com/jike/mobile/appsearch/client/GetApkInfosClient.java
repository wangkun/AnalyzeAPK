package com.jike.mobile.appsearch.client;

import com.jike.mobile.appsearch.thirft.ApkFullProperty;
//import com.jike.mobile.appsearch.thirft.ApkSimpleProperty;
import com.jike.mobile.appsearch.thirft.GetApkInfo;
import com.jike.mobile.appsearch.util.CommonUtils;
import com.jike.mobile.appsearch.util.analyzeAds;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetApkInfosClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String apkPath="D:\\apks\\apks9631\\apks9631\\wsv.slayton.apk";
        
        getAPKinfo(apkPath);
//        String filePathString="D:/apks/apks9631/apks9631";
//        File list=new File("D:/apks/apks9631/list.txt");
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(  
//                    new FileInputStream("D:/apks/apks9631/list.txt")));
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }  
//  
//        try {
//            for (String line = br.readLine(); line != null; line = br.readLine()) {  
//                getAPKinfo(filePathString+"/"+line); 
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }  
//        try {
//            br.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }  
//        analyzeAds.SortMapValue();
    }
    public static void getAPKinfo(String apkPath) {
        
      //IP host port
        TTransport transport = new TSocket("localhost",7911);
        
        try {
            transport.open();
        } catch (TTransportException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TProtocol protocol = new TBinaryProtocol(transport);
        GetApkInfo.Client client = new GetApkInfo.Client(protocol);
        
//        ApkSimpleProperty apkSimpleProperty = new ApkSimpleProperty();
        ApkFullProperty apkFullProperty = null;
        try {
//            apkSimpleProperty = client.getApkSimpleProperty(apkPath);
            apkFullProperty = client.getApkFullProperty(apkPath);
            if (apkFullProperty==null) {
                System.err.println("client.getApkFullProperty(apkPath) ==null ");
                return;
            }
        } catch (TException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        transport.close();
        if (apkFullProperty==null) {
            System.err.println("client.getApkFullProperty(apkPath) ==null ");
            return;
        }
        String iconPath="./iconFile/"+apkFullProperty.packageName+".png";
        if (apkFullProperty.packageName!=null) {
            File iconFile = CommonUtils.WriteByteBufferToFile(apkFullProperty.icon, iconPath);
        }
        printProperty(apkFullProperty);
        analyzeAds.getAdsFrequency(apkFullProperty.AdsList);

    }
    public static void printProperty(ApkFullProperty apkFullProperty) {
        System.out.println("getPackageName "+apkFullProperty.getPackageName());
        System.out.println("getVersionName "+apkFullProperty.getVersionName());
        System.out.println("getVersionCode "+apkFullProperty.getVersionCode());
        System.out.println("getMinSDK "+apkFullProperty.getMinSDK());
        System.out.println("getTargetSDK "+apkFullProperty.getTargetSDK());
        System.out.println("getUsesPermissonList "+apkFullProperty.getUsesPermissonList());
        System.out.println("getUsesFeatureList "+apkFullProperty.getUsesFeatureList());
        System.out.println("isSmallScreen "+apkFullProperty.isSmallScreen());
        System.out.println("isNormalScreen "+apkFullProperty.isNormalScreen());
        System.out.println("isLargeScreen "+apkFullProperty.isLargeScreen());
        System.out.println("isXlargeScreen "+apkFullProperty.isXlargeScreen());
        System.out.println("getSignature "+apkFullProperty.getSignature());
        System.out.println("getAdsList "+apkFullProperty.getAdsList());
        System.out.println("getAppNameMap "+apkFullProperty.getAppName());
        System.out.println("getApkSize "+apkFullProperty.getApkSize());
        
    }

}
