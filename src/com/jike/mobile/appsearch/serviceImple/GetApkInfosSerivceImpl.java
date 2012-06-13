package com.jike.mobile.appsearch.serviceImple;
import com.jike.mobile.appsearch.datebase.ResultInfoBuilder;
import com.jike.mobile.appsearch.thirft.ApkFullProperty;
import com.jike.mobile.appsearch.thirft.GetApkInfo;
import com.jike.mobile.appsearch.util.ApkInfoProperty;
import com.jike.mobile.appsearch.util.GetApkInfos;
import com.jike.mobile.appsearch.util.ManifestProperty;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

public class GetApkInfosSerivceImpl implements GetApkInfo.Iface{
    private static final Logger log = LogManager.getLogger(GetApkInfosSerivceImpl.class);
//    @Override
//    public ApkSimpleProperty getApkSimpleProperty(String apkPath) throws TException {
//        ApkSimpleProperty apkSimpleProperty = new ApkSimpleProperty();
////        ManifestProperty manifestProperty = GetApkInfos.getManifestProperty(apkPath);
//        ManifestProperty manifestProperty = DecompileXML.getManifestProperty(apkPath);
//        apkSimpleProperty.packageName=manifestProperty.getPackageName();
//        apkSimpleProperty.versionName=manifestProperty.getVersionName();
//        apkSimpleProperty.versionCode=manifestProperty.getVersionCode();
//        apkSimpleProperty.usesPermissonList=manifestProperty.getUsesPermissonArrayList();
//        apkSimpleProperty.usesFeatureList=manifestProperty.getUsesFeatureArrayList();
//        
//        apkSimpleProperty.minSDK=manifestProperty.getMinSdkVersion();
//        apkSimpleProperty.targetSDK=manifestProperty.getTargetSdkVersion();
//        
//        apkSimpleProperty.smallScreen=manifestProperty.isSmallScreen();
//        apkSimpleProperty.normalScreen=manifestProperty.isNormalScreen();
//        apkSimpleProperty.largeScreen=manifestProperty.isLargeScreen();
//        apkSimpleProperty.xlargeScreen=manifestProperty.isxLargeScreen();
//        
//        apkSimpleProperty.signature=manifestProperty.getSignature();
//        return apkSimpleProperty;
//    }

    @Override
    public ApkFullProperty getApkFullProperty(String apkKey) throws TException {
        ApkFullProperty apkFullProperty = new ApkFullProperty();
        log.debug("getApkFullProperty apkKey="+apkKey);
        ResultInfoBuilder.insert(apkKey);
        if (apkKey.equalsIgnoreCase("")||apkKey==null||apkKey.length()<1) {
            log.debug("apkPath==null");
            return apkFullProperty;
        }
        ApkInfoProperty apkInfoProperty = GetApkInfos.getApkInfoProperty(apkKey);
        if (apkInfoProperty==null&&apkFullProperty.packageName.length()<1) {
            System.err.println("get failed @ getApkInfoProperty");
            return apkFullProperty;
        }
        ManifestProperty manifestProperty = apkInfoProperty.getManifestProperty();
        
        apkFullProperty.packageName=manifestProperty.getPackageName();
        apkFullProperty.versionName=manifestProperty.getVersionName();
        apkFullProperty.versionCode=manifestProperty.getVersionCode();
        apkFullProperty.usesPermissonList=manifestProperty.getUsesPermissonArrayList();
        apkFullProperty.usesFeatureList=manifestProperty.getUsesFeatureArrayList();
        
        apkFullProperty.minSDK=manifestProperty.getMinSdkVersion();
        apkFullProperty.targetSDK=manifestProperty.getTargetSdkVersion();
        
        apkFullProperty.smallScreen=manifestProperty.isSmallScreen();
        apkFullProperty.normalScreen=manifestProperty.isNormalScreen();
        apkFullProperty.largeScreen=manifestProperty.isLargeScreen();
        apkFullProperty.xlargeScreen=manifestProperty.isxLargeScreen();
        //TODO
        apkFullProperty.signature=apkInfoProperty.getMD5();
        if (apkInfoProperty.getIconStream()!=null) {
            apkFullProperty.icon = apkInfoProperty.getIconStream();
        }
        apkFullProperty.appName = apkInfoProperty.getAppNameMap();
        apkFullProperty.AdsList = apkInfoProperty.getAdsList();
        apkFullProperty.apkSize = apkInfoProperty.getApkSize();
        apkFullProperty.securityLevel = apkInfoProperty.getSecurityLevel();
        apkFullProperty.setUpdateTimeIsSet(true);
        apkFullProperty.updateTime = apkInfoProperty.getMakeTime();
      //TODO: AppName
        if (apkFullProperty.packageName.length()>1) {
            ResultInfoBuilder.updateSuccess(apkKey);
        }
        return apkFullProperty;
    }

}
