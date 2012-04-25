package com.jike.mobile.appsearch.util;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Map;

public class ApkInfoProperty {
    
//    13: required binary icon;
//14: required map<string,string> appName; 
//15: required i32 securityLevel;
//16: required bool isHasAds=false;
    ManifestProperty manifestProperty = null;
    ByteBuffer iconStream = null;
    Map<String, String> appNameMap;
    int securityLevel = 0 ;
    boolean isHasAds=false;
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
    public boolean isHasAds() {
        return isHasAds;
    }
    public void setHasAds(boolean isHasAds) {
        this.isHasAds = isHasAds;
    }
    
}
