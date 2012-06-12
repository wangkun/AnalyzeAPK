package com.jike.mobile.appsearch.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApkInfoProperty {
    
//    13: required binary icon;
//14: required map<string,string> appName; 
//15: required i32 securityLevel;
//16: required bool isHasAds=false;
    
    ManifestProperty manifestProperty = new ManifestProperty();
    ByteBuffer iconStream = null;
    Map<String, String> appNameMap = new HashMap<String, String>();
    ArrayList<String> adsList = new ArrayList<String>();
    Double apkSize=0.0;
    int securityLevel = 0 ;
    String MD5="";
    String updateTime = "";
    
    public ApkInfoProperty() {
        // TODO Auto-generated constructor stub
        manifestProperty = new ManifestProperty();
        try {
            iconStream = ByteBuffer.wrap(FileUtils.readFileToByteArray(new File(Constants.DEFALUT_ICON)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        appNameMap = new HashMap<String, String>();
        adsList = new ArrayList<String>();
        apkSize=0.0;
        securityLevel = 0 ;
        MD5="";
    }
    
    public String getMakeTime() {
        return updateTime;
    }

    public void setMakeTime(String makeTime) {
        this.updateTime = makeTime;
    }

    public String getMD5() {
        return MD5;
    }
    public void setMD5(String mD5) {
        MD5 = mD5;
    }
    //    boolean isHasAds=false;
//    HashMap<String, String> adsMap = new HashMap<String, String>();
    public ManifestProperty getManifestProperty() {
        return manifestProperty;
    }
    public void setManifestProperty(ManifestProperty manifestProperty) {
        this.manifestProperty = manifestProperty;
    }
    public ByteBuffer getIconStream() {
        return iconStream;
    }
    public void setIconStream(ByteBuffer iconStream) {
        this.iconStream = iconStream;
    }
    public Map<String, String> getAppNameMap() {
        return appNameMap;
    }
    public void setAppNameMap(Map<String, String> appNameMap) {
        this.appNameMap = appNameMap;
    }
    public int getSecurityLevel() {
        return securityLevel;
    }
    public void setSecurityLevel(int securityLevel) {
        this.securityLevel = securityLevel;
    }
//    public boolean isHasAds() {
//        return isHasAds;
//    }
//    public void setHasAds(boolean isHasAds) {
//        this.isHasAds = isHasAds;
//    }
    public ArrayList<String> getAdsList() {
        return adsList;
    }
    public void setAdsList(ArrayList<String> adsList) {
        this.adsList = adsList;
    }
    public Double getApkSize() {
        return apkSize;
    }
    public void setApkSize(Double apkSize) {
        this.apkSize = apkSize;
    }
    
}
