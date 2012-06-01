package com.jike.mobile.appsearch.util;

import java.util.ArrayList;


public class ManifestProperty {
    
    
    String packageName = "";
    String versionName = "";
    String versionCode = "";
//    String Signature;
    //TO DO : permission list ; Screen support ; minSDK; targetSDK; use feature List;

    public String getPackageName() {
        return packageName;
    }


    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


    public String getVersionName() {
        return versionName;
    }


    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }


    public String getVersionCode() {
        return versionCode;
    }


    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }


//    public String getSignature() {
//        return Signature;
//    }


//    public void setSignature(String signature) {
//        Signature = signature;
//    }


    String iconString="";
    String appNameString="";
    
    public String getIconString() {
        return iconString;
    }


    public void setIconString(String iconString) {
        this.iconString = iconString;
    }


    public String getAppName() {
        return appNameString;
    }


    public void setAppName(String appNameString) {
        this.appNameString = appNameString;
    }

    ArrayList<String> usesPermissonArrayList = new ArrayList<String>();
    ArrayList<String> usesFeatureArrayList = new ArrayList<String>();

    
    public ArrayList<String> getUsesPermissonArrayList() {
        return usesPermissonArrayList;
    }


    public void setUsesPermissonArrayList(ArrayList<String> usesPermissionArrayList) {
        this.usesPermissonArrayList = usesPermissionArrayList;
    }


    public ArrayList<String> getUsesFeatureArrayList() {
        return usesFeatureArrayList;
    }


    public void setUsesFeatureArrayList(ArrayList<String> usesFeatureArrayList) {
        this.usesFeatureArrayList = usesFeatureArrayList;
    }

    int minSdkVersion = 0;
    int targetSdkVersion = 0;
    boolean smallScreen = true;
    boolean normalScreen = true;
    boolean largeScreen = true;
    boolean xlargeScreens = true;
    
    public int getMinSdkVersion() {
        return minSdkVersion;
    }


    public void setMinSdkVersion(int minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }


    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }


    public void setTargetSdkVersion(int targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }


    public boolean isSmallScreen() {
        return smallScreen;
    }


    public void setSmallScreen(boolean smallScreen) {
        this.smallScreen = smallScreen;
    }


    public boolean isNormalScreen() {
        return normalScreen;
    }


    public void setNormalScreen(boolean normalScreen) {
        this.normalScreen = normalScreen;
    }


    public boolean isLargeScreen() {
        return largeScreen;
    }


    public void setLargeScreen(boolean largeScreen) {
        this.largeScreen = largeScreen;
    }


    public boolean isxLargeScreen() {
        return xlargeScreens;
    }


    public void setxLargeScreen(boolean xlargeScreens) {
        this.xlargeScreens = xlargeScreens;
    }


    

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub.
        

    }

}
