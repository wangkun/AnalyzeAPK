package com.jike.mobile.appsearch.util;

import java.util.ArrayList;


public class ManifestProperty {
    
    
    private String PackageName;
    private String VersionName;
    private String VersionCode;
    private String Signature;
    //TO DO : permission list ; Screen support ; minSDK; targetSDK; use feature List;

    public String getPackageName() {
        return PackageName;
    }


    public void setPackageName(String packageName) {
        PackageName = packageName;
    }


    public String getVersionName() {
        return VersionName;
    }


    public void setVersionName(String versionName) {
        VersionName = versionName;
    }


    public String getVersionCode() {
        return VersionCode;
    }


    public void setVersionCode(String versionCode) {
        VersionCode = versionCode;
    }


    public String getSignature() {
        return Signature;
    }


    public void setSignature(String signature) {
        Signature = signature;
    }


    private String iconString;
    private String appNameString;
    
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

    private ArrayList<String> usesPermissonArrayList = new ArrayList<String>();
    private ArrayList<String> usesFeatureArrayList = new ArrayList<String>();

    
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

    private int minSdkVersion = 0;
    private int targetSdkVersion = 0;
    private boolean smallScreen = true;
    private boolean normalScreen = true;
    private boolean largeScreen = true;
    private boolean xlargeScreens = true;
    
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
