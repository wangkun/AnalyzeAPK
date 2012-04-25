package com.jike.mobile.appsearch.util;


import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ApkXMLParser {
    
    static ArrayList<String> userPermissionArrayList = new ArrayList<String>();
    static ArrayList<String> userFeatureArrayList = new ArrayList<String>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String xmlPathString="D:\\wk\\workspace\\AnalyzeAPK\\com.oupeng.mini.android_113728\\res\\values\\strings.xml";//"D:\\APK反编译\\AdyClock_0.9.6\\res\\values-ru\\strings.xml";
        File xmlFile = new File(xmlPathString);
        parserStringsXML(xmlFile, "app_name");
    }
    public static String parserStringsXML(File stringsXMLFile,String appNameString){
        String appName = null;
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(stringsXMLFile);
            Element root = document.getRootElement();
            for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
                Element element = i.next();
                // Analyze string
                if ("string".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("name")!=null&&element.attributeValue("name").equals(appNameString)) {
                        appName = element.getText() ;
                        System.out.println("getText: "+appName);
                        return appName;
                    }
                }
            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appName;
    }
    public static ManifestProperty parserManifestXML(File xmlFile) {

        SAXReader saxReader = new SAXReader();
        ManifestProperty manifestProperty = new ManifestProperty();
        try {
            Document document = saxReader.read(xmlFile);
            Element root = document.getRootElement();
//            fetch package name
            if (root.attribute("package")!=null) {
                manifestProperty.setPackageName(root.attributeValue("package"));
            }
            if (root.attribute("versionCode")!=null) {
                manifestProperty.setVersionCode(root.attributeValue("versionCode"));
            }
            if (root.attribute("versionName")!=null) {
                manifestProperty.setVersionName(root.attributeValue("versionName"));
            }
            

            for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
                Element element = i.next();
                List<Attribute> attrList = element.attributes();
//                fetch icon ; app name
                if ("application".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("icon")!=null) {
                        manifestProperty.setIconString(element.attributeValue("icon").split("/")[1]);
                    }
                    if (element.attribute("label")!=null) {
                        String appname = element.attributeValue("label");
                        appname = appname.startsWith("@")?appname.split("/")[1]:appname;
                        manifestProperty.setAppName(appname);
                    }
                }
                // Analyze permissions
                if ("uses-permission".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("name")!=null) {
                        userPermissionArrayList.add(element.attributeValue("name"));
                    }
                }
//                fetch sdk required
                if ("uses-sdk".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("minSdkVersion")!=null) {
//                        TODO: get minSdkVersion
                        element.attributeValue("minSdkVersion");
                    }
                    if (element.attribute("targetSdkVersion")!=null) {
//                        TODO: get targetSdkVersion
                        element.attributeValue("targetSdkVersion");
                    }
                }
//                fetch uses-feature
                if ("uses-feature".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("name")!=null) {
                        if (element.attribute("required")!=null) {
                            if ("true".equalsIgnoreCase(element.attributeValue("required"))) {
                                userFeatureArrayList.add(element.attributeValue("name"));
                            }
                        }else {
                            userFeatureArrayList.add(element.attributeValue("name"));
                        }
                    }
                }
                if ("uses-sdk".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("minSdkVersion")!=null) {
                        manifestProperty.setMinSdkVersion(Integer.parseInt(element.attributeValue("minSdkVersion")));
                    }
                    if (element.attribute("targetSdkVersion")!=null) {
                        manifestProperty.setMinSdkVersion(Integer.parseInt(element.attributeValue("targetSdkVersion")));
                    }
                }
                if ("supports-screens".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("smallScreens")!=null) {
                        manifestProperty.setSmallScreen(Boolean.parseBoolean(element.attributeValue("smallScreens")));
                    }
                    if (element.attribute("normalScreens")!=null) {
                        manifestProperty.setNormalScreen(Boolean.parseBoolean(element.attributeValue("normalScreens")));
                    }
                    if (element.attribute("largeScreens")!=null) {
                        manifestProperty.setLargeScreen(Boolean.parseBoolean(element.attributeValue("largeScreens")));
                    }
                    if (element.attribute("xlargeScreens")!=null) {
                        manifestProperty.setxLargeScreen(Boolean.parseBoolean(element.attributeValue("xlargeScreens")));
                    }
                }
            }
            manifestProperty.setUsesPermissonArrayList(userPermissionArrayList);
            manifestProperty.setUsesFeatureArrayList(userFeatureArrayList);
            
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String waitSHstr = manifestProperty.getPackageName() + ":"
                + manifestProperty.getVersionCode() + ":" + manifestProperty.getVersionName()
                + "\n" + getSig.getSignatureString(xmlFile.getAbsolutePath());
        manifestProperty.setSignature(DigestUtils.shaHex(waitSHstr).toUpperCase());
        return manifestProperty;
    }

}
