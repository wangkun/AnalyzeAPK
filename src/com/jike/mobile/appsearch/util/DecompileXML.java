
package com.jike.mobile.appsearch.util;


import org.xmlpull.v1.XmlPullParser;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import zip.tool.Decompression;

import android.content.res.local.AXmlResourceParser;
import android.util.local.TypedValue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DecompileXML {
    private static final Logger log = LogManager.getLogger(DecompileXML.class);
    // private static final Logger sLog =
    // LogManager.getLogger(ManifestXMLParser.class);

    private static ArrayList<String> userPermissionArrayList = new ArrayList<String>();

    private static ArrayList<String> userFeatureArrayList = new ArrayList<String>();

    private static InputStream getManifeStream(String apkPath) {
        InputStream stream = null;
        Decompression decompression = new Decompression(apkPath);
        stream = decompression.getAndroidManifest();
        return stream;
    }

    private static String decomplierManifest(InputStream stream) {
        log.debug("decomplierManifest start");
        StringBuffer buffer = new StringBuffer();
        if (stream == null) {
            buffer.append("Usage: AXMLPrinter <binary xml file>");
            return buffer.toString();
        }
        try {
            AXmlResourceParser parser = new AXmlResourceParser();
            parser.open(stream);
            StringBuilder indent = new StringBuilder(10);
            final String indentStep = "   ";
            while (true) {
                int type = parser.next();
                if (type == XmlPullParser.END_DOCUMENT) {
                    break;
                }
                switch (type) {
                    case XmlPullParser.START_DOCUMENT: {
                        buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                        break;
                    }
                    case XmlPullParser.START_TAG: {
                        buffer.append(String.format("%s<%s%s", indent,
                                getNamespacePrefix(parser.getPrefix()), parser.getName()));
                        indent.append(indentStep);

                        int namespaceCountBefore = parser.getNamespaceCount(parser.getDepth() - 1);
                        int namespaceCount = parser.getNamespaceCount(parser.getDepth());
                        for (int i = namespaceCountBefore; i != namespaceCount; ++i) {
                            buffer.append(String.format("%sxmlns:%s=\"%s\"", indent,
                                    parser.getNamespacePrefix(i), parser.getNamespaceUri(i)));
                        }

                        for (int i = 0; i != parser.getAttributeCount(); ++i) {
                            buffer.append(String.format("%s%s%s=\"%s\"", indent,
                                    getNamespacePrefix(parser.getAttributePrefix(i)),
                                    parser.getAttributeName(i), getAttributeValue(parser, i)));
                        }
                        buffer.append(String.format("%s>", indent));
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        indent.setLength(indent.length() - indentStep.length());
                        buffer.append(String.format("%s</%s%s>", indent,
                                getNamespacePrefix(parser.getPrefix()), parser.getName()));
                        break;
                    }
                    case XmlPullParser.TEXT: {
                        buffer.append(String.format("%s%s", indent, parser.getText()));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.debug("decomplierManifest end");
        return buffer.toString();
    }

    private static String getNamespacePrefix(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            return "";
        }
        return prefix + ":";
    }

    private static String getAttributeValue(AXmlResourceParser parser, int index) {
        int type = parser.getAttributeValueType(index);
        int data = parser.getAttributeValueData(index);
        if (type == TypedValue.TYPE_STRING) {
            return parser.getAttributeValue(index);
        }
        if (type == TypedValue.TYPE_ATTRIBUTE) {
            return String.format("?%s%08X", getPackage(data), data);
        }
        if (type == TypedValue.TYPE_REFERENCE) {
            return String.format("@%s%08X", getPackage(data), data);
        }
        if (type == TypedValue.TYPE_FLOAT) {
            return String.valueOf(Float.intBitsToFloat(data));
        }
        if (type == TypedValue.TYPE_INT_HEX) {
            return String.format("0x%08X", data);
        }
        if (type == TypedValue.TYPE_INT_BOOLEAN) {
            return data != 0 ? "true" : "false";
        }
        if (type == TypedValue.TYPE_DIMENSION) {
            return Float.toString(complexToFloat(data))
                    + DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
        }
        if (type == TypedValue.TYPE_FRACTION) {
            return Float.toString(complexToFloat(data))
                    + FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
        }
        if (type >= TypedValue.TYPE_FIRST_COLOR_INT && type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return String.format("#%08X", data);
        }
        if (type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT) {
            return String.valueOf(data);
        }
        return String.format("<0x%X, type 0x%02X>", data, type);
    }

    private static String getPackage(int id) {
        if (id >>> 24 == 1) {
            return "android:";
        }
        return "";
    }

    public static float complexToFloat(int complex) {
        return (float)(complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4) & 3];
    }

    private static final float RADIX_MULTS[] = {
            0.00390625F, 3.051758E-005F, 1.192093E-007F, 4.656613E-010F
    };

    private static final String DIMENSION_UNITS[] = {
            "px", "dip", "sp", "pt", "in", "mm", "", ""
    };

    private static final String FRACTION_UNITS[] = {
            "%", "%p", "", "", "", "", "", ""
    };

    public static ManifestProperty getManifestProperty(String apkPath) {
        log.debug("getManifestProperty");
        ManifestProperty manifestProperty = new ManifestProperty();

        SAXReader saxReader = new SAXReader();
        ByteArrayInputStream bais = new ByteArrayInputStream(decomplierManifest(
                getManifeStream(apkPath)).getBytes());
        // System.out.println(bais.available());
        try {
            Document document = saxReader.read(bais);
            Element root = document.getRootElement();
            // fetch package name
            if (root.attribute("package") != null) {
                manifestProperty.setPackageName(root.attributeValue("package"));
            }
            if (root.attribute("versionCode") != null) {
                manifestProperty.setVersionCode(root.attributeValue("versionCode"));
            }
            if (root.attribute("versionName") != null) {
                manifestProperty.setVersionName(root.attributeValue("versionName"));
            }

            for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
                Element element = i.next();
                List<Attribute> attrList = element.attributes();
                // fetch icon ; app name
                if ("application".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("icon") != null&&element.attributeValue("icon").contains("/")) {
                        manifestProperty
                                .setIconString(element.attributeValue("icon").split("/")[1]);
                    }
                    if (element.attribute("name") != null) {
                        String appname = element.attributeValue("name");
                        appname = appname.startsWith("@") ? appname.split("/")[1] : appname;
                        manifestProperty.setAppName(appname);
                    }
                }
                // Analyze permissions
                if ("uses-permission".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("name") != null) {
                        userPermissionArrayList.add(element.attributeValue("name"));
                    }
                }
                // fetch uses-feature
                if ("uses-feature".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("name") != null) {
                        if (element.attribute("required") != null) {
                            if ("true".equalsIgnoreCase(element.attributeValue("required"))) {
                                userFeatureArrayList.add(element.attributeValue("name"));
                            }
                        } else {
                            userFeatureArrayList.add(element.attributeValue("name"));
                        }
                    }
                }
                if ("uses-sdk".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("minSdkVersion") != null) {
                        manifestProperty.setMinSdkVersion(Integer.parseInt(element
                                .attributeValue("minSdkVersion")));
                    }
                    if (element.attribute("targetSdkVersion") != null) {
                        manifestProperty.setMinSdkVersion(Integer.parseInt(element
                                .attributeValue("targetSdkVersion")));
                    }
                }
                if ("supports-screens".equalsIgnoreCase(element.getName())) {
                    if (element.attribute("smallScreens") != null) {
                        manifestProperty.setSmallScreen(Boolean.parseBoolean(element
                                .attributeValue("smallScreens")));
                    }
                    if (element.attribute("normalScreens") != null) {
                        manifestProperty.setNormalScreen(Boolean.parseBoolean(element
                                .attributeValue("normalScreens")));
                    }
                    if (element.attribute("largeScreens") != null) {
                        manifestProperty.setLargeScreen(Boolean.parseBoolean(element
                                .attributeValue("largeScreens")));
                    }
                    if (element.attribute("xlargeScreens") != null) {
                        manifestProperty.setxLargeScreen(Boolean.parseBoolean(element
                                .attributeValue("xlargeScreens")));
                    }
                }
            }
            manifestProperty.setUsesPermissonArrayList(userPermissionArrayList);
            manifestProperty.setUsesFeatureArrayList(userFeatureArrayList);

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        log.debug("getSignature start");
//        String waitSHstr = manifestProperty.getPackageName() + ":"
//                + manifestProperty.getVersionCode() + ":" + manifestProperty.getVersionName()
//                + "\n" + getSig.getSignatureString(apkPath);
//        manifestProperty.setSignature(DigestUtils.shaHex(waitSHstr).toUpperCase());
//        log.debug("getSignature end");
        return manifestProperty;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub.
        // sLog.debug("Test ManifestXMLParser main");
        String apkPath = "D:\\apks\\AppSearch_Android_1426l.apk";
        ManifestProperty mManifestProperty = getManifestProperty(apkPath);
        System.out.println("mManifestProperty.getPackageName: "
                + mManifestProperty.getPackageName());
        System.out.println("mManifestProperty.getVersionCode: "
                + mManifestProperty.getVersionCode());
        System.out.println("mManifestProperty.getVersionName: "
                + mManifestProperty.getVersionName());
    }

}
