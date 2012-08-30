package com.jike.mobile.appsearch.datebase;



import com.jike.mobile.appsearch.thirft.ApkFullProperty;
import com.jike.mobile.appsearch.util.CommonUtils;
import com.jike.mobile.appsearch.util.Constants;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResultInfoBuilder {
    
    /*
     * 
     CREATE  TABLE `appsearch_mobile`.`analyzeapk_result` (

  `signature` VARCHAR(255) NOT NULL ,

  `apksize` DOUBLE NOT NULL DEFAULT 0 ,

  `visitcount` INT NOT NULL DEFAULT 1 ,
  
  `success` INT NOT NULL DEFAULT 0 ,
  
  `analyzetime` DOUBLE NOT NULL DEFAULT 0 ,
  
  `time` timestamp NOT NULL ,

  PRIMARY KEY (`signature`) )ENGINE=InnoDB DEFAULT CHARSET=utf8;
     */

    private static final Logger log = LogManager
            .getLogger(ResultInfoBuilder.class);
//    final static String host = "127.0.0.1";
//    final static String databaseName = "jike";
//    final static String databaseUserName = "root";
//    final static String databaseUserPassword = "123456";
    final static String proFilePath = Constants.DBINFO_PROPERTIES;//"DBInfo.properties";
    static String host = "127.0.0.1";
    static String host_bk = "58.68.249.9";
    static String databaseName = "appsearch_mobile";
    static String databaseUserName = "jike";
    static String databaseUserPassword = "jikemobile";
    static String tableName = "analyzeapk_result";
    
    private static void initDB() {
        // TODO Auto-generated method stub
        host = CommonUtils.getPropertiesValue("host", proFilePath);
        host_bk = CommonUtils.getPropertiesValue("host_bk", proFilePath);
        databaseName = CommonUtils.getPropertiesValue("databaseName", proFilePath);
        databaseUserName = CommonUtils.getPropertiesValue("databaseUserName", proFilePath);
        databaseUserPassword = CommonUtils.getPropertiesValue("databaseUserPassword", proFilePath);
//        tableName = CommonUtils.getPropertiesValue("tableName", proFilePath);
    }
    public static DatabaseManager ConnectDB(){
        initDB();
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                ) {
            System.err
                    .println("Parameter error in function ConnectDB");
            log.error("Parameter error in function ConnectDB");
            return null;
        }

        DatabaseManager databaseManager = new DatabaseManager();
        
        databaseManager.setDriverName("com.mysql.jdbc.Driver");
        databaseManager.setConnectionString("jdbc:mysql://"+host+":3306/"
                + databaseName + "?useUnicode=true&characterEncoding=utf8");
        databaseManager.setUserName(databaseUserName);
        databaseManager.setPassword(databaseUserPassword);
        int re = databaseManager.connectDatabase();
//        log.debug("databaseManager.connectDatabase()  "
//                + re);
        if (re==-1) {
            log.debug("retry host_bk");
            databaseManager.setConnectionString("jdbc:mysql://"+host_bk+":3306/"
                    + databaseName + "?useUnicode=true&characterEncoding=utf8");
            databaseManager.setUserName(databaseUserName);
            databaseManager.setPassword(databaseUserPassword);
            re = databaseManager.connectDatabase();
            if(re==-1){   
                log.debug("host_bk databaseManager.connectDatabase()  "
                        + re);
                return null;
            }
        }
        return databaseManager;
    }
    public static DatabaseManager ConnectDBforWrite(){
        initDB();
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                ) {
            System.err
                    .println("Parameter error in function ConnectDB");
            log.error("Parameter error in function ConnectDB");
            return null;
        }

        DatabaseManager databaseManager = new DatabaseManager();
        
        databaseManager.setDriverName("com.mysql.jdbc.Driver");
        databaseManager.setConnectionString("jdbc:mysql://"+host+":3306/"
                + databaseName + "?useUnicode=true&characterEncoding=utf8");
        databaseManager.setUserName(databaseUserName);
        databaseManager.setPassword(databaseUserPassword);
        int re = databaseManager.connectDatabase();
//        log.debug("databaseManager.connectDatabase()  "  + re);
        if (re==-1) {
            log.debug("retry ConnectDBforWrite");
            databaseManager.setConnectionString("jdbc:mysql://"+host_bk+":3306/"
                    + databaseName + "?useUnicode=true&characterEncoding=utf8");
            databaseManager.setUserName(databaseUserName);
            databaseManager.setPassword(databaseUserPassword);
            re = databaseManager.connectDatabase();
            if(re==-1){   
                log.debug("host_bk databaseManager.connectDatabase()  "
                        + re);
                return null;
            }
        }
        return databaseManager;
    }
    
    public static DatabaseManager ConnectDBforRead(){
        initDB();
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                ) {
            System.err
                    .println("Parameter error in function ConnectDB");
            log.error("Parameter error in function ConnectDB");
            return null;
        }

        DatabaseManager databaseManager = new DatabaseManager();
        
        databaseManager.setDriverName("com.mysql.jdbc.Driver");
        databaseManager.setConnectionString("jdbc:mysql://"+host_bk+":3306/"
                + databaseName + "?useUnicode=true&characterEncoding=utf8");
        databaseManager.setUserName(databaseUserName);
        databaseManager.setPassword(databaseUserPassword);
        int re = databaseManager.connectDatabase();
//        log.debug("databaseManager.connectDatabase()  "  + re);
        if (re==-1) {
            log.debug("retry ConnectDBforRead");
            databaseManager.setConnectionString("jdbc:mysql://"+host+":3306/"
                    + databaseName + "?useUnicode=true&characterEncoding=utf8");
            databaseManager.setUserName(databaseUserName);
            databaseManager.setPassword(databaseUserPassword);
            re = databaseManager.connectDatabase();
            if(re==-1){   
                log.debug("host_bk databaseManager.connectDatabase()  "
                        + re);
                return null;
            }
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

    
    public static boolean checkApkIsAnalyzed(String signature) {
        boolean return_value=false;
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                || signature == null ) {
            System.err
                    .println("Parameter error in function HandleMarketInfoData");
            log.error("Parameter error in function HandleMarketInfoData");
            return false;
        }
        int rs;
        DatabaseManager databaseManager = ConnectDBforRead();
        if (databaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return false;
        }
        try {
            rs =1;
            rs = databaseManager.checkTableExist(tableName);
            if (rs == 1) {
                String sql = "SELECT * " +
                        "from "+ tableName + " WHERE signature='"+signature+"';";
                PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql);
                ResultSet re = pstmt.executeQuery(); 
                if (re.next()) {
                    pstmt.close();
                    return_value=true;
                }else {
                    pstmt.close();
                    return_value=false;
                }
            }
            
            if (rs == -1) {
                System.err.println("Table in database is error");
                log.error("Table in database is error");
                return_value=false;
            }
        } catch (Exception e) {
            log.error("select  analysis result faild :"
                    + e.getMessage());
            return_value=false;
        } 
      finally {
          try {
              if (databaseManager != null) {
                  if (databaseManager.closeDatabase() == -1) {
                      log.error("Execute function closeDatabase is error");
                      return_value=false;
                  }
              }
          } finally {
              databaseManager = null;
          }
      }

        return return_value;        
    }
    
    public static boolean updateApksize(String signature,double apksize){
        boolean return_value=false;
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                || signature == null ) {
            System.err
                    .println("Parameter error in function HandleMarketInfoData");
            log.error("Parameter error in function HandleMarketInfoData");
            return false;
        }
        int rs;
        DatabaseManager databaseManager = ConnectDBforWrite();
        if (databaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return false;
        }
        try {
            rs =1;
            rs = databaseManager.checkTableExist(tableName);
            if (rs == 1) {
                String updataSql = "UPDATE " + tableName + " SET apksize = " + apksize + " "
                        + "WHERE signature='" + signature + "';" + "";
                int upInsert = databaseManager.execute(updataSql);
//                log.debug("upInsert=" + upInsert + ";updataSql=" + updataSql);
                // "UPDATE "+ tableName +
                // " SET icon = ? where signature='"+signature+"';";
                return_value=true;
            }
            
            if (rs == -1) {
                System.err.println("Table in database is error");
                log.error("Table in database is error");
                return_value=false;
            }
        } catch (Exception e) {
            log.error("select  analysis result faild :"
                    + e.getMessage());
            return_value=false;
        } 
      finally {
          try {
              if (databaseManager != null) {
                  if (databaseManager.closeDatabase() == -1) {
                      log.error("Execute function closeDatabase is error");
                      return_value=false;
                  }
              }
          } finally {
              databaseManager = null;
          }
      }
        return return_value;
    }
    public static boolean updateAnalyzetime(String signature,double analyzetime){
        boolean return_value=false;
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                || signature == null ) {
            System.err
                    .println("Parameter error in function HandleMarketInfoData");
            log.error("Parameter error in function HandleMarketInfoData");
            return false;
        }
        int rs;
        DatabaseManager databaseManager = ConnectDBforWrite();
        if (databaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return false;
        }
        try {
            rs =1;
            rs = databaseManager.checkTableExist(tableName);
            
            if (rs == 1) {
                String updataSql = "UPDATE " + tableName + " SET analyzetime = " + analyzetime
                        + " " + "WHERE signature='" + signature + "';" + "";
                int upInsert = databaseManager.execute(updataSql);
//                log.debug("upInsert=" + upInsert + ";updataSql=" + updataSql);
                // "UPDATE "+ tableName +
                // " SET icon = ? where signature='"+signature+"';";
                return_value = true;
            }
            
            if (rs == -1) {
                System.err.println("Table in database is error");
                log.error("Table in database is error");
                return_value=false;
            }
        } catch (Exception e) {
            log.error("select  analysis result faild :"
                    + e.getMessage());
            return_value=false;
        } 
      finally {
          try {
              if (databaseManager != null) {
                  if (databaseManager.closeDatabase() == -1) {
                      log.error("Execute function closeDatabase is error");
                      return_value=false;
                  }
              }
          } finally {
              databaseManager = null;
          }
      }
        return return_value;
    }
    
    public static boolean updateSuccess(String signature){
        boolean return_value=false;
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                || signature == null ) {
            System.err
                    .println("Parameter error in function HandleMarketInfoData");
            log.error("Parameter error in function HandleMarketInfoData");
            return false;
        }
        int rs;
        DatabaseManager databaseManager = ConnectDBforWrite();
        if (databaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return false;
        }
        try {
            rs =1;
            rs = databaseManager.checkTableExist(tableName);
            if (rs == 1) {
                String updataSql = "UPDATE " + tableName + " SET success = " + 1 + " "
                        + "WHERE signature='" + signature + "';" + "";
                int upInsert = databaseManager.execute(updataSql);
//                log.debug("reInsert=" + upInsert + ";updataSql=" + updataSql);
                // "UPDATE "+ tableName +
                // " SET icon = ? where signature='"+signature+"';";
                return_value=true;
            }
            
            if (rs == -1) {
                System.err.println("Table in database is error");
                log.error("Table in database is error");
                return_value=false;
            }
        } catch (Exception e) {
            log.error("select  analysis result faild :"
                    + e.getMessage());
            return_value=false;
        } 
      finally {
          try {
              if (databaseManager != null) {
                  if (databaseManager.closeDatabase() == -1) {
                      log.error("Execute function closeDatabase is error");
                      return_value=false;
                  }
              }
          } finally {
              databaseManager = null;
          }
      }
        return return_value;
    }
    
    
    public static boolean insert(String signature){
        boolean return_value=false;
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null || tableName == null
                || signature == null ) {
            System.err
                    .println("Parameter error in function HandleMarketInfoData");
            log.error("Parameter error in function HandleMarketInfoData");
            return false;
        }
        int rs;
        DatabaseManager databaseManager = ConnectDBforWrite();
        if (databaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return false;
        }
        try {
            rs =1;
            rs = databaseManager.checkTableExist(tableName);
            if (rs == 1) {
                String sql = "SELECT visitcount " +
                        "from "+ tableName + " WHERE signature='"+signature+"';";
                ResultSet re = databaseManager.executeQuery(sql); 
                if (re.next()) {
                    int visitcount=re.getInt("visitcount");
                    visitcount++;
                    String updataSql="UPDATE " + tableName + " SET visitcount = "+visitcount+" " +
                    		"WHERE signature='"+signature+"';"+
                    		"";
                    int upInsert = databaseManager.execute(updataSql);
//                    log.debug("reInsert="+upInsert+";updataSql="+updataSql);
                    //"UPDATE "+ tableName + " SET icon = ? where signature='"+signature+"';";
                    
                    return_value=true;
                }else {
                    
                    String insertSql = "INSERT INTO " + tableName +
                    		" (`signature`) VALUES ('"+signature+"');" ;
                    //INSERT INTO `appsearch_mobile`.`analyzeapk_result` (`signature`) VALUES ('asdf');
                    int reInsert = databaseManager.execute(insertSql);
//                    log.debug("reInsert="+reInsert+";insertSql="+insertSql);
                    return_value=true;
                }
            }
            
            if (rs == -1) {
                System.err.println("Table in database is error");
                log.error("Table in database is error");
                return_value=false;
            }
        } catch (Exception e) {
            log.error("select  analysis result faild :"
                    + e.getMessage());
            return_value=false;
        } 
      finally {
          try {
              if (databaseManager != null) {
                  if (databaseManager.closeDatabase() == -1) {
                      log.error("Execute function closeDatabase is error");
                      return_value=false;
                  }
              }
          } finally {
              databaseManager = null;
          }
      }
        return return_value;
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
        String signature = "test";
        insert(signature);
        updateAnalyzetime(signature, System.currentTimeMillis());
        updateApksize(signature, 1231.2);
        updateSuccess(signature);
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
