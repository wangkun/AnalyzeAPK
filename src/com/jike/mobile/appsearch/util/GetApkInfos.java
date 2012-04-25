
package com.jike.mobile.appsearch.util;

import brut.androlib.AndrolibException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.awt.List;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetApkInfos {
    private static final Logger log = LogManager.getLogger(GetApkInfos.class); 

    private static String apkPath;

    public final static String POSTFIX = ".apk";

    static File[] decompileFiles;

    private static int flag = 0;

    private static String outputPath = "";

    /**
     * @param args
     */
    public static void main(String[] args) {
        apkPath = "D:\\apks\\AppSearch_Android_1426l.apk";
        // decompileApk(apkPath);
        getApkInfoProperty(apkPath);
    }

    private static File decompileApk(String apkPath) {
        
        File rootFile = null;
        if (!apkPath.endsWith(POSTFIX)) {
            return null;
        }
        File apkFile = new File(apkPath);
        log.debug("decompileApk Start……"+apkFile.getAbsolutePath());
        String appName = (apkFile.getName().substring(0, apkFile.getName().length() - 4)).trim();
        try {
            if (apkFile.exists()) {
                brut.apktool.Main.main(new String[] {
                        "decode", "-f", apkFile.getAbsolutePath(), outputPath + appName
                });
                rootFile = new File(outputPath + appName);
                flag = 0;
            } else {
                flag = 1;
            }
        } catch (AndrolibException e) {
            e.printStackTrace();
            flag = 1;
        } catch (IOException e) {
            e.printStackTrace();
            flag = 1;
        } catch (InterruptedException e) {
            e.printStackTrace();
            flag = 1;
        }
        log.debug("decompileApk end……");
        return rootFile;
    }

    private static File getIconRes(File file, String iconString) {
        File maxIconFile = null;
        ArrayList<File> iconFileList = new ArrayList<File>();
        File resFile = new File(file.getAbsolutePath() + "/" + "res");
        try {
            if (!FileUtils.directoryContains(file, resFile)) {
                return null;
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
                if (FileUtils.directoryContains(drawableFile, iconFile)) {
                    iconFileList.add(iconFile);
                }
            }
            File drawableFile = new File(file.getAbsolutePath() + "/res/drawable");
            File iconFile = new File(drawableFile.getAbsolutePath() + "/" + iconString + ".png");
            System.out.println(drawableFile.getAbsolutePath());

            if (FileUtils.directoryContains(drawableFile, iconFile)) {
                System.out.println("find icon");
            } else {
                System.err.println("cann't find icon res ");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

        return maxIconFile;

    }
    public static Map<String, String> getAppNameMap(File file, String appNameString) {
        Map<String, String>  appNameMap = new HashMap<String, String>();//<String, String>;
        File resFile = new File(file.getAbsolutePath() + "/" + "res");
        try {
            if (!FileUtils.directoryContains(file, resFile)) {
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
                if (FileUtils.directoryContains(valuesFile, stringsFile)) {
                    String appName = ApkXMLParser.parserStringsXML(stringsFile, appNameString);
                    appNameMap.put(fileName, appName);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appNameMap;
    }

    public static ApkInfoProperty getApkInfoProperty(String apkPath) {
        ApkInfoProperty apkInfoProperty = new ApkInfoProperty();
        File rootFile = decompileApk(apkPath);
        ManifestProperty manifestProperty = getManifestProperty(rootFile);
        apkInfoProperty.manifestProperty = manifestProperty;
        File iconFile = getIconRes(rootFile,manifestProperty.getIconString());
//        iconFile.
        try {
            apkInfoProperty.iconStream = ByteBuffer.wrap(FileUtils.readFileToByteArray(iconFile));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(getAppNameMap(rootFile, manifestProperty.getAppName()));
        apkInfoProperty.appNameMap = getAppNameMap(rootFile, manifestProperty.getAppName());
        CommonUtils.deleteFiles(rootFile);
        log.debug(" delete rootFile " + rootFile.getAbsolutePath());
        return apkInfoProperty;
    }
    public static ManifestProperty getManifestProperty(File rootFile) {
//        File rootFile = decompileApk(apkPath);
        log.debug("getManifestProperty Start……");
        ManifestProperty mManifestProperty = new ManifestProperty();
        File manifestXMLFile = null;
        decompileFiles = rootFile.listFiles(new FilenameFilter() {
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
