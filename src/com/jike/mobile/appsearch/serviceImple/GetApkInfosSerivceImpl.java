package com.jike.mobile.appsearch.serviceImple;
import com.jike.mobile.appsearch.thirft.*;
import com.jike.mobile.appsearch.util.ApkInfoProperty;
import com.jike.mobile.appsearch.util.DecompileXML;
import com.jike.mobile.appsearch.util.GetApkInfos;
import com.jike.mobile.appsearch.util.ManifestProperty;

import org.apache.thrift.TException;

public class GetApkInfosSerivceImpl implements GetApkInfo.Iface{
    @Override
    public ApkSimpleProperty getApkSimpleProperty(String apkPath) throws TException {
        ApkSimpleProperty apkSimpleProperty = new ApkSimpleProperty();
//        ManifestProperty manifestProperty = GetApkInfos.getManifestProperty(apkPath);
        ManifestProperty manifestProperty = DecompileXML.getManifestProperty(apkPath);
        apkSimpleProperty.packageName=manifestProperty.getPackageName();
        apkSimpleProperty.versionName=manifestProperty.getVersionName();
        apkSimpleProperty.versionCode=manifestProperty.getVersionCode();
        apkSimpleProperty.usesPermissonList=manifestProperty.getUsesPermissonArrayList();
        apkSimpleProperty.usesFeatureList=manifestProperty.getUsesFeatureArrayList();
        
        apkSimpleProperty.minSDK=manifestProperty.getMinSdkVersion();
        apkSimpleProperty.targetSDK=manifestProperty.getTargetSdkVersion();
        
        apkSimpleProperty.smallScreen=manifestProperty.isSmallScreen();
        apkSimpleProperty.normalScreen=manifestProperty.isNormalScreen();
        apkSimpleProperty.largeScreen=manifestProperty.isLargeScreen();
        apkSimpleProperty.xlargeScreen=manifestProperty.isxLargeScreen();
        
        apkSimpleProperty.signature=manifestProperty.getSignature();
        return apkSimpleProperty;
    }

    @Override
    public ApkFullProperty getApkFullProperty(String apkPath) throws TException {
        ApkFullProperty apkFullProperty = new ApkFullProperty();
        ApkInfoProperty apkInfoProperty = GetApkInfos.getApkInfoProperty(apkPath);
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
        apkFullProperty.signature=manifestProperty.getSignature();
        
        apkFullProperty.icon = apkInfoProperty.getIconStream();
        apkFullProperty.appName = apkInfoProperty.getAppNameMap();
      //TODO: AppName
        return apkFullProperty;
    }

}