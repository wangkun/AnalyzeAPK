
package com.jike.mobile.appsearch.util;

import com.jike.mobile.appsearch.datebase.ApkInfoBuilder;
import com.jike.mobile.appsearch.thirft.ApkFullProperty;

import brut.androlib.AndrolibException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetApkInfos {
    private static final Logger log = LogManager.getLogger(GetApkInfos.class); 


    public final static String POSTFIX = ".apk";



    private static String outputPath = "./deFiles/";


    private static String defalut_icon = "defalut_icon.png";

    /**
     * @param args
     */
    public static void main(String[] args) {
        String apkPath = "D:\\apks\\carpenter.apk";
        // decompileApk(apkPath);
        apkPath="D:\\apks\\apks9631\\apks9631\\wsv.slayton.apk";
        apkPath = "12154871019707775886";
//        System.out.println("getApkMD5 = "+getApkMD5(apkPath));
        getApkInfoProperty(apkPath);
    }

    
    
    private static File decompileApk(File apkFile) {
        
        File rootFile = null;
        if (!apkFile.getName().endsWith(POSTFIX)) {
            return null;
        }
//        File apkFile = new File(apkPath);
        log.debug("decompileApk Start……"+apkFile.getAbsolutePath());
        String appName = (apkFile.getName().substring(0, apkFile.getName().length() - 4)).trim();
        try {
            if (apkFile.exists()) {
                brut.apktool.Main.main(new String[] {
                        "d", "-f", apkFile.getAbsolutePath(), outputPath + appName
                });
                rootFile = new File(outputPath + appName);
            } else {
            }
        } catch (AndrolibException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("decompileApk end……");
        return rootFile;
    }

    private static File getIconRes(File file, String iconString) {
        File maxIconFile = null;
        ArrayList<File> iconFileList = new ArrayList<File>();
        File resFile = new File(file.getAbsolutePath() + "/" + "res");
        if (!resFile.exists()&&iconString==null) {
            maxIconFile=new File(defalut_icon);
            return maxIconFile;
        }
        File[] files = resFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.startsWith("drawable")) {
                    return true;
                }
                return false;
            }
        });
        for (File drawableFile : files) {
            File iconFile = new File(drawableFile.getAbsolutePath() + "/" + iconString + ".png");
            if (iconFile.exists()) {
                iconFileList.add(iconFile);
                System.out.println("find icon .");
            }
        }
        // get bigest icon;
        long maxSize = 0;
        
        for (File f : iconFileList) {
            // FileUtils.getFile(directory, names)
            if (maxSize < f.length()) {
                maxIconFile = f;
                maxSize = f.length();
            }
            System.out.println(f.getAbsolutePath() + ". size : " + f.length());
        }

        return maxIconFile==null?new File(defalut_icon):maxIconFile;

    }
    public static String getAppVersionName(File file, String appNameString) {
        String appVersionName = "";
        File resFile = new File(file.getAbsolutePath() + "/" + "res/values/strings.xml");
        if (resFile.exists()) {
            appVersionName = ApkXMLParser.parserStringsXML(resFile, appNameString);
        }
        return appVersionName;
    }
    public static Map<String, String> getAppNameMap(File file, String appNameString) {
        Map<String, String>  appNameMap = new HashMap<String, String>();//<String, String>;
        File resFile = new File(file.getAbsolutePath() + "/" + "res");
        if (!resFile.exists()) {
            return null;
        }
        File[] files = resFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.startsWith("values")) {
                    return true;
                }
                return false;
            }
        });
        for (File valuesFile : files) {
            String fileName = valuesFile.getName();
            File stringsFile = new File(valuesFile.getAbsoluteFile()+ "/" + "strings.xml");
            if (stringsFile.exists()) {
                String appName = ApkXMLParser.parserStringsXML(stringsFile, appNameString);
                appNameMap.put(fileName, appName);
            }
        }
        return appNameMap;
    }

    public static String getApkMD5(String apkPath ) {
        File apkFile = new File(apkPath);
        return getApkMD5(apkFile);
    }
    public static String getApkMD5(File apkFile ) {
        String hashcodeString = "";
        try {
            hashcodeString = getApkMD5(new FileInputStream(apkFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return hashcodeString;
    }

    public static String getApkMD5(InputStream apkFileStream) {
        String hashcodeString = "";
        try {
            hashcodeString = DigestUtils.md5Hex(apkFileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashcodeString;
    }
    public static ApkInfoProperty getApkInfoProperty(String apkPath) {
            File apkFile = GetApkFileFromCassandra.getAPK(apkPath);
//        apkFile = getApkFileByKey(apkPath);//TODO 
            if (!apkFile.exists()) {
                return null;
            }
        return getApkInfoProperty(apkFile);
    }
    
//    public static File getApkFileByKey(String key){
//        File apkFile = null;
//        apkFile=GetApkFileFromCassandra.getAPK(key);
//        
//        return apkFile;
//    }
    
    public static ApkInfoProperty getApkInfoProperty(File apkFile) {
        
        
        ApkInfoProperty apkInfoProperty = new ApkInfoProperty();
        if (!apkFile.exists()) {
            return null;
        }
        apkInfoProperty.MD5 = getApkMD5(apkFile);
        //TODO verify MD5, if exist in DB, then , return DB result;
        boolean isAnalyzed=ApkInfoBuilder.checkApkIsAnalyzed(apkInfoProperty.MD5);
        if (isAnalyzed) {
            log.debug("isAnalyzed");
            ApkFullProperty apkFullProperty = ApkInfoBuilder.getAPKInfoFromDB(apkInfoProperty.MD5);
            ManifestProperty manifestProperty = new ManifestProperty();
            manifestProperty.packageName=apkFullProperty.packageName;
            manifestProperty.versionName=apkFullProperty.versionName;
            manifestProperty.versionCode=apkFullProperty.versionCode;
            
            manifestProperty.usesPermissonArrayList=(ArrayList<String>)apkFullProperty.usesPermissonList;
            manifestProperty.usesFeatureArrayList=(ArrayList<String>)apkFullProperty.usesFeatureList;
            
            manifestProperty.minSdkVersion=apkFullProperty.minSDK;
            manifestProperty.targetSdkVersion=apkFullProperty.targetSDK;
            
            manifestProperty.smallScreen=apkFullProperty.smallScreen;
            manifestProperty.normalScreen=apkFullProperty.normalScreen;
            manifestProperty.largeScreen=apkFullProperty.largeScreen;
            manifestProperty.xlargeScreens=apkFullProperty.xlargeScreen;
            
            apkInfoProperty.manifestProperty = manifestProperty;
            
            apkInfoProperty.MD5 = apkFullProperty.signature;
            apkInfoProperty.iconStream=apkFullProperty.icon;
            apkInfoProperty.appNameMap = apkFullProperty.appName;
            apkInfoProperty.adsList=(ArrayList<String>)apkFullProperty.AdsList;
            apkInfoProperty.apkSize=apkFullProperty.apkSize;
            apkInfoProperty.securityLevel=apkFullProperty.securityLevel;
            log.debug("get from DB");
            return apkInfoProperty;
        }
        File rootFile = decompileApk(apkFile);
        if (rootFile==null) {
            System.err.println("failed @ rootFile==null");
            return null;
        }
        ManifestProperty manifestProperty = getManifestProperty(rootFile);
        if (manifestProperty.versionName.startsWith("@string")) {
            manifestProperty.versionName=getAppVersionName(rootFile, manifestProperty.versionName.split("/")[1]);
        }
        
        apkInfoProperty.manifestProperty = manifestProperty;
        File iconFile = getIconRes(rootFile,manifestProperty.getIconString());
        log.debug("getIconRes");
//        iconFile.
        if (iconFile==null&&iconFile.length()<1) {
            System.err.println("iconFile==null");
            return null;
        }
        try {
            apkInfoProperty.iconStream = ByteBuffer.wrap(FileUtils.readFileToByteArray(iconFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("getAppNameMap "+getAppNameMap(rootFile, manifestProperty.getAppName()));
        apkInfoProperty.appNameMap = getAppNameMap(rootFile, manifestProperty.getAppName());
        apkInfoProperty.adsList = DetectAds.getAdsList(rootFile.getAbsolutePath());
        log.debug("getAdsList");
        apkInfoProperty.apkSize = (double)apkFile.length()/1000;
        
        if (!isAnalyzed) {
            ApkFullProperty apkFullProperty = new ApkFullProperty();
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
            
            apkFullProperty.icon = apkInfoProperty.getIconStream();
            apkFullProperty.appName = apkInfoProperty.getAppNameMap();
            apkFullProperty.AdsList = apkInfoProperty.getAdsList();
            apkFullProperty.apkSize = apkInfoProperty.getApkSize();
            apkFullProperty.securityLevel = apkInfoProperty.getSecurityLevel();
            ApkInfoBuilder.SaveApkInfoToDB(apkFullProperty);
            log.debug("save to DB");
        }
        log.debug("delete begin ..");
        CommonUtils.deleteFiles(rootFile);
        log.debug(" delete rootFile " + rootFile.getAbsolutePath());
        //TODO
        if (!Constants.debugAtLocal) {
//            CommonUtils.deleteFiles(apkFile);
        }
//        CommonUtils.deleteFiles(apkFile);
        return apkInfoProperty;
    }
    
    public static ManifestProperty getManifestProperty(File rootFile) {
//        File rootFile = decompileApk(apkPath);
        log.debug("getManifestProperty Start……");
        ManifestProperty mManifestProperty = new ManifestProperty();
        File manifestXMLFile = null;
        if (rootFile.exists()==false) {
            return null;
        }
        File[]  decompileFiles = rootFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File arg0, String arg1) {
                if ("res".equalsIgnoreCase(arg1)) {
                    return false;
                }
                return true;
            }
        });
        for (File f : decompileFiles) {
            if ("AndroidManifest.xml".equalsIgnoreCase(f.getName())) {
                // prms = PermissionParser.parserXml(f);
                manifestXMLFile = f;
            }
        }
        mManifestProperty = ApkXMLParser.parserManifestXML(manifestXMLFile);
        System.out.println("getAppName @ "+mManifestProperty.getAppName());
        
        

        log.debug("getManifestProperty End……");
        return mManifestProperty;
    }
    
}
