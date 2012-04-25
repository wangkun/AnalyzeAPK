package com.jike.mobile.appsearch.client;

import com.jike.mobile.appsearch.thirft.ApkFullProperty;
import com.jike.mobile.appsearch.thirft.ApkSimpleProperty;
import com.jike.mobile.appsearch.thirft.GetApkInfo;
import com.jike.mobile.appsearch.util.CommonUtils;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.File;

public class GetApkInfosClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String apkPath="D:/apks/AppSearch_Android_1426l.apk";
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
        
        ApkSimpleProperty apkSimpleProperty = new ApkSimpleProperty();
        ApkFullProperty apkFullProperty = new ApkFullProperty();
        try {
//            apkSimpleProperty = client.getApkSimpleProperty(apkPath);
            apkFullProperty = client.getApkFullProperty(apkPath);
        } catch (TException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        transport.close();
        String iconPath=apkFullProperty.packageName+".png";
        if (apkFullProperty.packageName!=null) {
            File iconFile = CommonUtils.WriteByteBufferToFile(apkFullProperty.icon, iconPath);
        }
        printProperty(apkFullProperty);
        System.out.println(apkFullProperty.getAppName());

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
        
    }

}
