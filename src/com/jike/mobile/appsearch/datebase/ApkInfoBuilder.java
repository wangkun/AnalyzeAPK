package com.jike.mobile.appsearch.datebase;



import com.jike.mobile.appsearch.thirft.ApkFullProperty;
import com.jike.mobile.appsearch.util.CommonUtils;
import com.jike.mobile.appsearch.util.analyzeAds;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jf.smali.smaliParser.nonvoid_type_descriptor_return;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApkInfoBuilder {

    private static final Logger log = LogManager
            .getLogger(ApkInfoBuilder.class);
    final static String host = "127.0.0.1";
    final static String databaseName = "jike";
    final static String databaseUserName = "root";
    final static String databaseUserPassword = "123456";
    final static String tableName = "analyzeapk";
    private static File iconFile = new File("defalut_icon.png");
    
    public static DatabaseManager ConnectDB(){
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                ) {
            System.err
                    .println("Parameter error in function HandleMarketInfoData");
            log.error("Parameter error in function HandleMarketInfoData");
            return null;
        }

        DatabaseManager databaseManager = new DatabaseManager();
        
        databaseManager.setDriverName("com.mysql.jdbc.Driver");
        databaseManager.setConnectionString("jdbc:mysql://"+host+":3306/"
                + databaseName + "?useUnicode=true&characterEncoding=utf8");
        databaseManager.setUserName(databaseUserName);
        databaseManager.setPassword(databaseUserPassword);
        int re = databaseManager.connectDatabase();
        log.debug("databaseManager.connectDatabase()  "
                + re);
        if (re==-1) {
            return null;
        }
        return databaseManager;
    }
    public static void CloseDB(DatabaseManager databaseManager){
        try {
            if (databaseManager != null) {
                if (databaseManager.closeDatabase() == -1) {
                    log.error("Execute function closeDatabase is error");
                    return ;
                }
            }
        } finally {
            databaseManager = null;
        }
    }
//    create database jike;
//    #show databases;
//    use jike;
//    CREATE  TABLE `jike`.`analyzeapk` (
//      `packagename` VARCHAR(255) NOT NULL ,
//      `versionname` VARCHAR(255) NULL ,
//      `versioncode` INT NULL ,
//      `usespermissonlist` TEXT NULL ,
//      `usesfeaturelist` TEXT NULL ,
//      `minsdk` INT NULL ,
//      `targetsdk` INT NULL ,
//      `smallscreen` INT NULL ,
//      `normalscreen` INT NULL ,
//      `largescreen` INT NULL ,
//      `xlargescreen` INT NULL ,
//      `signature` VARCHAR(255) NOT NULL ,
//      `icon` MEDIUMBLOB NULL ,
//      `appname_en` VARCHAR(255) NULL ,
//      `appname_cn` VARCHAR(255) NULL ,
//      `adslist` TEXT NULL ,
//      `apksize` DOUBLE NULL ,
//      `securitylevel` INT NULL ,
//      PRIMARY KEY (`signature`, `packagename`) )ENGINE=InnoDB DEFAULT CHARSET=utf8;
    public static void SaveApkInfoToDB(ApkFullProperty apkFullProperty){
        if(apkFullProperty==null&&apkFullProperty.icon==null){
            System.err.println("failed @ SaveApkInfoToDB apkFullProperty.icon==null ");
            return;
        }
        
        String appname_cn="",appname_en="";
        if (apkFullProperty.appName.containsKey("values-zh-rCN")) {
            appname_cn=apkFullProperty.appName.get("values-zh-rCN");
        }
        if (apkFullProperty.appName.containsKey("values")) {
        appname_en=apkFullProperty.appName.get("values");
        }
        
        HandlePrivacyInfoData(apkFullProperty.packageName, apkFullProperty.versionName, apkFullProperty.versionCode,
                apkFullProperty.usesPermissonList.toString(), apkFullProperty.usesFeatureList.toString(), 
                apkFullProperty.minSDK, apkFullProperty.targetSDK, 
                apkFullProperty.smallScreen?1:0, apkFullProperty.normalScreen?1:0, apkFullProperty.largeScreen?1:0, apkFullProperty.xlargeScreen?1:0,
                apkFullProperty.signature, apkFullProperty.icon, 
                appname_en, appname_cn, 
                apkFullProperty.AdsList.toString(), apkFullProperty.apkSize, apkFullProperty.securityLevel);
    }

    public static int HandlePrivacyInfoData(
            final String packagename, 
            final String versionname, 
            final String versioncode,
            
            final String usespermissonlist,
            final String usesfeaturelist,
            final int minsdk,
            final int targetsdk,
            //
            final int smallscreen,
            final int normalscreen,
            final int largescreen,
            final int xlargescreen,
            //
            final String signature,
            final ByteBuffer icon,
            final String appname_en,
            final String appname_cn,
            
            final String adslist,
            final double apksize,
            final int securitylevel
            ) 
    {
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                || signature == null || ( packagename == null)) {
            System.err
                    .println("Parameter error in function HandleMarketInfoData");
            log.error("Parameter error in function HandleMarketInfoData");
            return -1;
        }
        String insertSql = null;
        int rs;
        DatabaseManager databaseManager = ConnectDB();

        if (databaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return -1;
        }

        try {
            rs =1;
            rs = databaseManager.checkTableExist(tableName);
            if (rs == 1) {
                insertSql = "insert into  " + tableName + " SET " +
                        "packagename='"+ packagename+"' , "+ 
                        "versionname='"+ versionname+"' , "+ 
                        "versioncode="+ versioncode+" , "+
                        
                        "usespermissonlist='"+ usespermissonlist+"' , "+
                        "usesfeaturelist='"+ usesfeaturelist+"' , "+
                        "minsdk="+ minsdk+" , "+
                        "targetsdk="+ targetsdk+" , "+
                        //
                        "smallscreen="+ smallscreen+" , "+
                        "normalscreen="+ normalscreen+" , "+
                        "largescreen="+ largescreen+" , "+
                        "xlargescreen="+ xlargescreen+" , "+
                        //
                        "signature='"+ signature+"' , "+
//                        "icon='"+ icon+"' , "+
                        "appname_en='"+ appname_en+"' , "+
                        "appname_cn='"+ appname_cn+"' , "+
                        
                        "adslist='"+ adslist+"' , "+
                        "apksize="+ apksize+" , "+
                        "securitylevel="+ securitylevel 
                        ;
//                CommonUtils.WriteByteBufferToFile(icon, "insert.png");
                
                rs = databaseManager.execute(insertSql);
                String sql = "UPDATE "+ tableName + " SET icon = ? where signature='"+signature+"';";
                PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql);
                pstmt.setBinaryStream(1, new ByteArrayInputStream(icon.array()), icon.remaining());
                rs=pstmt.executeUpdate(); 
                pstmt.close();
                
                
            }
            log.debug("insert sql is " + insertSql);
            if (rs == -1) {
                System.err.println("Table in database is error");
                log.error("Table in database is error");
                return -1;
            }
        } catch (SQLException e) {
            log.error("Insert  analysis result faild :"
                    + e.getMessage());
            return -1;
        } 
      finally {
          try {
              if (databaseManager != null) {
                  if (databaseManager.closeDatabase() == -1) {
                      log.error("Execute function closeDatabase is error");
                      return -1;
                  }
              }
          } finally {
              databaseManager = null;
          }
      }
        return 1;
    }
    
    public static boolean checkApkIsAnalyzed(String signature) {
        
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                || signature == null ) {
            System.err
                    .println("Parameter error in function HandleMarketInfoData");
            log.error("Parameter error in function HandleMarketInfoData");
            return false;
        }
        int rs;
        DatabaseManager databaseManager = ConnectDB();
        if (databaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return false;
        }
        try {
            rs =1;
            rs = databaseManager.checkTableExist(tableName);
            if (rs == 1) {
                String sql = "SELECT packagename,versionname, versioncode," +
                        "usespermissonlist,usesfeaturelist," +
                        "minsdk,targetsdk," +
                        "smallscreen,normalscreen,largescreen,xlargescreen," +
                        "signature,icon," +
                        "appname_en,appname_cn," +
                        "adslist,apksize,securitylevel " +
                        "from "+ tableName + " WHERE signature='"+signature+"';";
                
                PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql);
                ResultSet re = pstmt.executeQuery(); 
                if (re.next()) {
                    pstmt.close();
                    return true;
                }else {
                    pstmt.close();
                    return false;
                }
            }
            
            if (rs == -1) {
                System.err.println("Table in database is error");
                log.error("Table in database is error");
                return false;
            }
        } catch (Exception e) {
            log.error("select  analysis result faild :"
                    + e.getMessage());
            return false;
        } 
      finally {
          try {
              if (databaseManager != null) {
                  if (databaseManager.closeDatabase() == -1) {
                      log.error("Execute function closeDatabase is error");
                      return false;
                  }
              }
          } finally {
              databaseManager = null;
          }
      }

        return false;        
    }
    
    public static ApkFullProperty getAPKInfoFromDB(String signature) {
        
        ApkFullProperty apkFullProperty = new ApkFullProperty();
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                || signature == null ) {
            System.err
                    .println("Parameter error in function HandleMarketInfoData");
            log.error("Parameter error in function HandleMarketInfoData");
            return apkFullProperty;
        }
        int rs;
        DatabaseManager databaseManager = ConnectDB();

        if (databaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return apkFullProperty;
        }

        try {
            rs =1;
            rs = databaseManager.checkTableExist(tableName);
            if (rs == 1) {
                
                 
                
                String sql = "SELECT packagename,versionname, versioncode," +
                		"usespermissonlist,usesfeaturelist," +
                		"minsdk,targetsdk," +
                		"smallscreen,normalscreen,largescreen,xlargescreen," +
                		"signature,icon," +
                		"appname_en,appname_cn," +
                		"adslist,apksize,securitylevel " +
                		"from "+ tableName + " WHERE signature='"+signature+"';";
                
                PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql);
                ResultSet re = pstmt.executeQuery(); 
                if (re.next()) {
                    apkFullProperty.packageName=re.getString("packageName");
                    apkFullProperty.versionName=re.getString("versionName");
                    apkFullProperty.versionCode=re.getInt("versionCode")+"";
                    apkFullProperty.usesPermissonList=CommonUtils.getArrayListFromString(re.getString("usespermissonlist"));
                    apkFullProperty.usesFeatureList=CommonUtils.getArrayListFromString(re.getString("usesFeatureList"));
                    apkFullProperty.minSDK=re.getInt("minsdk");
                    apkFullProperty.targetSDK=re.getInt("targetsdk");
                    apkFullProperty.smallScreen=re.getInt("smallscreen")==1;
                    apkFullProperty.normalScreen=re.getInt("normalScreen")==1;
                    apkFullProperty.largeScreen=re.getInt("largeScreen")==1;
                    apkFullProperty.xlargeScreen=re.getInt("xlargeScreen")==1;
                    apkFullProperty.signature=re.getString("signature");
//                  ByteArray  apkFullProperty.icon=TODO
                    InputStream is = re.getBinaryStream("icon");
                    if(is!=null&&IOUtils.toByteArray(is).length>10){
                        apkFullProperty.icon = ByteBuffer.wrap(IOUtils.toByteArray(is));
                    }else {
                        apkFullProperty.icon = ByteBuffer.wrap(FileUtils.readFileToByteArray(iconFile));
                    }
                    Map<String, String> appnameMap = new HashMap<String, String>();
                    if (!re.getString("appname_cn").equalsIgnoreCase("null")&&re.getString("appname_cn").length()>0) {
                        appnameMap.put("values-zh-rCN", re.getString("appname_cn"));
                    }
                    appnameMap.put("values", re.getString("appname_en"));
                    apkFullProperty.appName = appnameMap;
                    
                    apkFullProperty.AdsList=CommonUtils.getArrayListFromString(re.getString("AdsList"));
                    apkFullProperty.apkSize=re.getDouble("apkSize");
                    apkFullProperty.securityLevel=re.getInt("securitylevel");
                }
                
                
                
                pstmt.close();
                log.debug("insert sql is " + sql);
                
            }
            
            if (rs == -1) {
                System.err.println("Table in database is error");
                log.error("Table in database is error");
                return apkFullProperty;
            }
        } catch (Exception e) {
            log.error("select  analysis result faild :"
                    + e.getMessage());
            return apkFullProperty;
        } 
      finally {
          try {
              if (databaseManager != null) {
                  if (databaseManager.closeDatabase() == -1) {
                      log.error("Execute function closeDatabase is error");
                      return apkFullProperty;
                  }
              }
          } finally {
              databaseManager = null;
          }
      }

        return apkFullProperty;        
    }
    
    
public static void getApkAdsFromDB() {
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                ) {
            System.err
                    .println("Parameter error in function HandleMarketInfoData");
            log.error("Parameter error in function HandleMarketInfoData");
            return ;
        }
        int rs;
        DatabaseManager databaseManager = ConnectDB();

        if (databaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return ;
        }

        try {
            rs =1;
            rs = databaseManager.checkTableExist(tableName);
            if (rs == 1) {
                String sql = "SELECT " +
                        "adslist " +
                        "FROM "+ tableName + " WHERE adslist IS NOT NULL ;";
                PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql);
                ResultSet re = pstmt.executeQuery(); 
                while (re.next()) {
                    System.out.println(re.getString("AdsList"));
                     ArrayList<String> AdsList = CommonUtils.getArrayListFromString(re.getString("AdsList"));
                     analyzeAds.getAdsFrequency(AdsList);
                }
                pstmt.close();
                log.debug("SELECT sql is " + sql);
                analyzeAds.SortMapValue();
                log.debug("SortMapValue " );
            }
            
            if (rs == -1) {
                System.err.println("Table in database is error");
                log.error("Table in database is error");
                return ;
            }
        } catch (Exception e) {
            log.error("select  analysis result faild :"
                    + e.getMessage());
            return ;
        } 
      finally {
          try {
              if (databaseManager != null) {
                  if (databaseManager.closeDatabase() == -1) {
                      log.error("Execute function closeDatabase is error");
                      return ;
                  }
              }
          } finally {
              databaseManager = null;
          }
      }

        return ;        
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
//        HandlePrivacyInfoData("com.jike.test", null, null, null, null, 0, 0, 0, 0, 0, 0, "qianming", 0, null, null, null, 0, 0);
//        File testFile = new File("app.netapex.looneytunes.png");
//        InputStream is = new FileInputStream(testFile);
//        printProperty(getAPKInfoFromDB("qianming"));
//        File iconFile = CommonUtils.WriteByteBufferToFile(getAPKInfoFromDB("qianming").icon, "haha.png");
//        System.out.println(checkApkIsAnalyzed("qianming"));
        getApkAdsFromDB();
    }
    

    public static void printProperty(ApkFullProperty apkFullProperty) {
        System.out.println("getPackageName "+apkFullProperty.getPackageName());
        System.out.println("getVersionName "+apkFullProperty.getVersionName());
        System.out.println("getVersionCode "+apkFullProperty.getVersionCode());
        System.out.println("getMinSDK "+apkFullProperty.getMinSDK());
        System.out.println("getTargetSDK "+apkFullProperty.getTargetSDK());
        System.out.println("getUsesPermissonList "+apkFullProperty.getUsesPermissonList());
        System.out.println("getUsesFeatureList "+apkFullProperty.getUsesFeatureList());
        System.out.println("isSmallScreen "+apkFullProperty.isSmallScreen());
        System.out.println("isNormalScreen "+apkFullProperty.isNormalScreen());
        System.out.println("isLargeScreen "+apkFullProperty.isLargeScreen());
        System.out.println("isXlargeScreen "+apkFullProperty.isXlargeScreen());
        System.out.println("getSignature "+apkFullProperty.getSignature());
        System.out.println("getAdsList "+apkFullProperty.getAdsList());
        System.out.println("getAppNameMap "+apkFullProperty.getAppName());
        System.out.println("getApkSize "+apkFullProperty.getApkSize());
        
    }
}