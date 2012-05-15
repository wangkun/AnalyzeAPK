package com.jike.mobile.appsearch.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApkInfoProperty {
    
//    13: required binary icon;
//14: required map<string,string> appName; 
//15: required i32 securityLevel;
//16: required bool isHasAds=false;
    ManifestProperty manifestProperty = null;
    ByteBuffer iconStream ;
    Map<String, String> appNameMap;
    ArrayList<String> adsList;
    Double apkSize=0.0;
    int securityLevel = 0 ;
    String MD5="";
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
