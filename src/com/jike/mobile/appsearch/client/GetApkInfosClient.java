package com.jike.mobile.appsearch.client;

import com.jike.mobile.appsearch.thirft.ApkFullProperty;
import com.jike.mobile.appsearch.thirft.GetApkInfo;
import com.jike.mobile.appsearch.util.CommonUtils;
import com.jike.mobile.appsearch.util.analyzeAds;

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
        String key="13851920794926929216";
        //" kill server 52448422673114159";//<-no manifest.xml; //"11317503348386965201";//"2k error 10496040029157976478";//"no icon 10988429586710037558";//"解析失败id=9094489175483464045";//"7205617736938680212";
        GetApkInfosClient.getAPKinfo(key);
        
        
//        String apk_key="failedFileKey.log";
//        String filePathString="D:/apks/apks9631/apks9631";
////        File list=new File("D:/apks/apks9631/list.txt");
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(  
//                    new FileInputStream(apk_key)));
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }  
//  
//        try {
//            for (String key = br.readLine(); key != null; key = br.readLine()) {  
//                getAPKinfo(key); 
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
        String localhost = "127.0.0.1";
        localhost="10.1.3.213";//"192.168.40.130";//14.44
        
        localhost="10.1.1.94";//"192.168.40.130";//14.44
        localhost="10.1.1.92";//"192.168.40.130";//14.44        
//        localhost="192.168.40.130";//vmware
      //IP host port
//        TTransport transport = new TFramedTransport( new TSocket("localhost",7911));
        TTransport transport = new TSocket(localhost,7911);
        
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
            System.out.println("iconFile"+iconFile.getAbsolutePath()+" size = "+ iconFile.length());
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
        System.out.println("getSecurityLevel "+apkFullProperty.getSecurityLevel());
        System.out.println("getUpdateTime "+apkFullProperty.getUpdateTime());
        
    }

}
