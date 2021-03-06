
package com.jike.mobile.appsearch.util;

import brut.androlib.AndrolibException;

import com.jike.mobile.appsearch.datebase.ApkInfoBuilder;
import com.jike.mobile.appsearch.datebase.ResultInfoBuilder;
import com.jike.mobile.appsearch.thirft.ApkFullProperty;

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



    private static String outputPath = Constants.OUTPUT_PATH;


    private static String defalut_icon = Constants.DEFALUT_ICON;

    /**
     * @param args
     */
    public static void main(String[] args) {
        String apkPath = "D:\\apks\\carpenter.apk";
        // decompileApk(apkPath);
        apkPath="D:\\apks\\apks9631\\apks9631\\wsv.slayton.apk";
        apkPath = "11317503348386965201";
//        System.out.println("getApkMD5 = "+getApkMD5(apkPath));
        getApkInfoProperty(apkPath);
    }

    
    
    private static File decompileApk(File apkFile) {
        
        File tempDeFiles=new File(outputPath);
        
        if (!tempDeFiles.exists()||!tempDeFiles.isDirectory()) {
            tempDeFiles.mkdir();
        }
        
        File rootFile = null;
        if (apkFile==null){
            log.error("decompileApk apkFile==null");
            return rootFile;
        }
        if (!apkFile.getName().endsWith(POSTFIX)) {
            return rootFile;
        }
//        File apkFile = new File(apkPath);
        log.debug("decompileApk Start……"+apkFile.getAbsolutePath());
        String appName = (apkFile.getName().substring(0, apkFile.getName().length() - 4)).trim();
        try {
            if (apkFile.exists()) {
                rootFile = new File(outputPath + appName);
                brut.apktool.Main.main(new String[] {
                        "d", "-f", apkFile.getAbsolutePath(), outputPath + appName
                });
            } else {
            }
        } catch (AndrolibException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
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
        if (files==null||files.length==0) {
            return new File(defalut_icon);
        }
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
        if (!appNameString.contains("@")||!appNameString.contains("/")) {
            appNameMap.put("values", appNameString);
            return appNameMap;
        }
        appNameString = appNameString.startsWith("@")?appNameString.split("/")[1]:appNameString;
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
                if (appName!=null&&appName.length()>1) {
                    appNameMap.put(fileName, appName);
                }
            }
        }
        return appNameMap;
    }

    public static String getApkMD5(String apkPath ) {
        File apkFile = new File(apkPath);
        String md5=getApkMD5(apkFile);
        return md5;
    }
    public static String getApkMD5(File apkFile ) {
        String hashcodeString = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(apkFile);
            hashcodeString = getApkMD5(fileInputStream);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return hashcodeString;
    }

    public static String getApkMD5(InputStream apkFileStream) {
        String hashcodeString = "";
        try {
            hashcodeString = DigestUtils.md5Hex(apkFileStream);
            apkFileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashcodeString;
    }
//    public static ApkInfoProperty getApkInfoProperty(String apkKey) {
//            File apkFile = GetApkFileFromCassandra.getAPK(apkKey);
////        apkFile = getApkFileByKey(apkPath);//TODO 
//            if (apkFile == null && !apkFile.exists()) {
//                return null;
//            }
//        return getApkInfoProperty(apkFile,apkKey);
//    }
    
//    public static File getApkFileByKey(String key){
//        File apkFile = null;
//        apkFile=GetApkFileFromCassandra.getAPK(key);
//        
//        return apkFile;
//    }
    
    public static ApkInfoProperty getApkInfoProperty(String apkKey) {
        
        long start=System.currentTimeMillis();
        ApkInfoProperty apkInfoProperty = new ApkInfoProperty();
        
        //TODO verify MD5, if exist in DB, then , return DB result;
        boolean isAnalyzed=ApkInfoBuilder.checkApkIsAnalyzed(apkKey);
        if (isAnalyzed) {
//            log.debug("isAnalyzed");
            ApkFullProperty apkFullProperty = ApkInfoBuilder.getAPKInfoFromDB(apkKey);
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
            apkInfoProperty.updateTime=apkFullProperty.updateTime;
            log.debug("get from DB");
            return apkInfoProperty;
        }
        File apkFile = GetApkFileFromCassandra.getAPK(apkKey,Constants.APKS_PATH);
        ResultInfoBuilder.updateApksize(apkKey, apkFile.length()/1000);
        final File rootFile = decompileApk(apkFile);
        Runnable deleteFilesRunnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
//                CommonUtils.forceDelete(apkFile);
                CommonUtils.deleteFiles(rootFile);
            }
        };
        Thread deleteThread = new Thread(deleteFilesRunnable);
        if (rootFile==null||!rootFile.exists()) {
            if (!apkFile.delete()) {
                CommonUtils.forceDelete(apkFile);
                log.error("forceDelete error @ " + apkFile.getName());
            }
            deleteThread.run();
            System.err.println("failed @ rootFile==null");
            return apkInfoProperty;
        }
        apkInfoProperty.MD5 = apkKey;
        ManifestProperty manifestProperty = getManifestProperty(rootFile);
        if (manifestProperty.versionName.startsWith("@string")) {
            manifestProperty.versionName=getAppVersionName(rootFile, manifestProperty.versionName.split("/")[1]);
        }
        
        apkInfoProperty.manifestProperty = manifestProperty;
        File iconFile = getIconRes(rootFile,manifestProperty.getIconString());
//        log.debug("getIconRes");
//        iconFile.
//        if (iconFile==null&&iconFile.length()<1) {
//            System.err.println("iconFile==null");
//            return null;
//        }
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
        
        apkInfoProperty.updateTime = "" + CommonUtils.getApkMakeTime(apkFile.getAbsolutePath());
        
        
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
            
            apkFullProperty.icon = apkInfoProperty.iconStream;
            apkFullProperty.appName = apkInfoProperty.getAppNameMap();
            apkFullProperty.AdsList = apkInfoProperty.getAdsList();
            apkFullProperty.apkSize = apkInfoProperty.getApkSize();
            apkFullProperty.securityLevel = apkInfoProperty.getSecurityLevel();
            
            apkFullProperty.updateTime = apkInfoProperty.getMakeTime();
            ApkInfoBuilder.SaveApkInfoToDB(apkFullProperty);
            log.debug("save to DB");
        }
//        log.debug("delete begin ..");
        
        
        
        CommonUtils.forceDelete(apkFile);
//        log.debug("forceDelete Apk end……");  
        
        
        deleteThread.run();
//        CommonUtils.deleteFiles(rootFile);
//        log.debug(" delete rootFile " + rootFile.getAbsolutePath());
        long useTime = System.currentTimeMillis()-start;
        ResultInfoBuilder.updateAnalyzetime(apkKey, useTime);
//        log.debug("useTime = " + useTime);
        return apkInfoProperty;
    }
    
    
    
    public static ManifestProperty getManifestProperty(File rootFile) {
//        File rootFile = decompileApk(apkPath);
//        log.debug("getManifestProperty Start……");
        ManifestProperty mManifestProperty = new ManifestProperty();
        File manifestXMLFile = null;
        if (rootFile.exists()==false) {
            return mManifestProperty;
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
        if (manifestXMLFile!=null) {
            mManifestProperty = ApkXMLParser.parserManifestXML(manifestXMLFile);
            System.out.println("getAppName @ "+mManifestProperty.getAppName());
//            log.debug("getManifestProperty End……");
        }
        return mManifestProperty;
    }
    
}
