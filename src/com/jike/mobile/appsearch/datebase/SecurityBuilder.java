package com.jike.mobile.appsearch.datebase;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SecurityBuilder {
    private static final Logger log = LogManager
            .getLogger(SecurityBuilder.class);
    
    static String host = "58.68.224.156";
    static String host_bk = "58.68.224.156";//"10.1.9.9";
    
    static String databaseUserName = "mes";
    static String databaseUserPassword = "wan@203AMAAS";
    
    static String databaseNameInput = "new_urldb";
    static String web_task = databaseNameInput+"."+"web_task";
    
    static String databaseNameRequest = "mesdb";
    static String Tbl_STaskRequest = databaseNameRequest+"."+"Tbl_STaskRequest";
    static String Tbl_STaskRequestLog = databaseNameRequest+"."+"Tbl_STaskRequestLog";
    static String Tbl_STaskSampleQueue = databaseNameRequest+"."+"Tbl_STaskSampleQueue";
    static String Tbl_STaskScanResult = databaseNameRequest+"."+"Tbl_STaskScanResult";
    /*
     #1
        insert into new_urldb.web_task(name, size, time, user, type, enginelist)
        values('file/testapk', 0, unix_timestamp(), 'admin', 'file','');select last_insert_id() ;
        #2
        insert into mesdb.Tbl_STaskRequest(SamSrcType, SamSrc, EngineList, Priority, RequestTime) 
        values(1,'58.68.224.155\nadmin\nroot@wanAMAAS\n21\nfile/testapk','',100,unix_timestamp());select last_insert_id() ;
        #3
        update new_urldb.web_task set task_id=57 where id = 199 and type = 'file';
        #4
        SELECT * FROM new_urldb.web_task ;
        #5 查看是否完成
        SELECT * FROM mesdb.Tbl_STaskRequestLog where STaskReqID = 57;
        #6 单个文件
        select * from mesdb.Tbl_STaskSampleQueue where STaskReqID = 57;
        #7 是否有毒
        select  *  from mesdb.Tbl_STaskScanResult where STaskReqID = 57;
        
        select  A.FilePath , B.MD5, B.Engine, B.VirusName  from mesdb.Tbl_STaskSampleQueue as A, mesdb.Tbl_STaskScanResult as B where B.STaskReqID = 57 and A.MD5=B.MD5;
    */
    public static int beginScanning(String apkFTPDir){
        int return_value = -1;
        DatabaseManager input_DatabaseManager = ConnectDB(host, databaseNameInput, databaseUserName, databaseUserPassword);
        DatabaseManager request_DatabaseManager = ConnectDB(host, databaseNameRequest, databaseUserName, databaseUserPassword);
        if (input_DatabaseManager == null || request_DatabaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return -1;
        }
        try {
            if (input_DatabaseManager.checkTableExist(web_task) == 1
                    && request_DatabaseManager.checkTableExist(Tbl_STaskRequest) == 1) {
                //                #1
//                insert into new_urldb.web_task(name, size, time, user, type, enginelist)
//                values('file/testapk', 0, unix_timestamp(), 'admin', 'file','');select last_insert_id() ;
                String insertSqlToWebTask = "insert into new_urldb.web_task(name, size, time, user, type, enginelist,task_id) " +
                		"values('file/" +
                		apkFTPDir + //"testapk" +
                		"', 0, unix_timestamp(), 'admin', 'file','', 0);";
                if (-1==input_DatabaseManager.execute(insertSqlToWebTask)) {
                    log.error("error sql :" + insertSqlToWebTask);
                }
                String Select_insert_id = "select last_insert_id() ";
                ResultSet rs_web_id = input_DatabaseManager.executeQuery(Select_insert_id);
                int web_id=-1;
                if (rs_web_id.next()) {
                    web_id = rs_web_id.getInt("last_insert_id()");
                }
//                #2
//                insert into mesdb.Tbl_STaskRequest(SamSrcType, SamSrc, EngineList, Priority, RequestTime) 
//                values(1,'58.68.224.155\nadmin\nroot@wanAMAAS\n21\nfile/testapk','',100,unix_timestamp());select last_insert_id() ;
                String insertSqlToTbl_STaskRequest = "insert into mesdb.Tbl_STaskRequest(SamSrcType, SamSrc, EngineList, Priority, RequestTime)" +
                		"values(1,'192.168.1.2\nadmin\nroot@wanAMAAS\n21\nfile/" +
                		apkFTPDir + //"testapk" +
                		"','',100,unix_timestamp());";
                if(-1 == request_DatabaseManager.execute(insertSqlToTbl_STaskRequest)) {
                    log.error("error sql :" + insertSqlToTbl_STaskRequest);
                }
                int task_id=-1;
                ResultSet rs_task_id = request_DatabaseManager.executeQuery(Select_insert_id);
                if (rs_task_id.next()) {
                    task_id = rs_task_id.getInt("last_insert_id()");
                }
//                #3
//                update new_urldb.web_task set task_id=57 where id = 199 and type = 'file';
                if (-1!=task_id && -1 != web_id) {
                    String updateSqlWebTask = "update new_urldb.web_task set task_id=" +
                            task_id +
                            " where id = " +
                            web_id+
                            " and type = 'file';";
                    if (-1 == input_DatabaseManager.execute(updateSqlWebTask)) {
                        log.error("input_DatabaseManager.execute failed : " + updateSqlWebTask );
                    }else {
                        return_value = task_id;
                    }
                }else {
                    log.error("updateSqlWebTask failed : -1" );
                }
            }else {
                log.error("table not exist : " + web_task);
            }
        } catch (Exception e) {
            log.error("begin scanning failed :" + e.getMessage());
            return_value = -1;
        }finally {
            try {
                if (input_DatabaseManager != null || request_DatabaseManager !=null) {
                    if (input_DatabaseManager.closeDatabase() == -1 || -1 == request_DatabaseManager.closeDatabase()) {
                        log.error("Execute function closeDatabase is error");
                        return_value = -1;
                    }
                }
            } finally {
                input_DatabaseManager = null;
                request_DatabaseManager = null;
            }
        }
        return return_value;
    }
    /**
     * 
     * @param STaskReqID
     * @return 1 if finished; 0 if unfinish; -1 if error;
     */
    public static int IsScanFinished(int STaskReqID) {
        int return_value = -1;
        if (STaskReqID == -1) {
            log.error("STaskReqID == -1");
            return return_value;
        }
        DatabaseManager request_DatabaseManager = ConnectDB(host, databaseNameRequest, databaseUserName, databaseUserPassword);
        if (request_DatabaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return return_value;
        }
//        #5 查看是否完成
//        SELECT count(*) FROM mesdb.Tbl_STaskRequestLog where STaskReqID = 57 and FinishTime != '';
        try {
            if (1 == request_DatabaseManager.checkTableExist(Tbl_STaskRequestLog)) {
                String checkSql = "SELECT count(*) FROM mesdb.Tbl_STaskRequest "
                        + "where STaskReqID = " + 
                        STaskReqID + // "57" +
                        " and STException=1;";
                ResultSet rs_check = request_DatabaseManager.executeQuery(checkSql);
                if (rs_check.next()) {
                    if (0 == rs_check.getInt("count(*)")) {
                        String selectSql = "SELECT count(*) FROM mesdb.Tbl_STaskRequestLog "
                                + "where STaskReqID = " + 
                                STaskReqID + // "57" +
                                " and FinishTime != '';";
                        ResultSet rs_finished = request_DatabaseManager.executeQuery(selectSql);

                        if (rs_finished.next()) {
                            if (1 == rs_finished.getInt("count(*)")) {
                                return_value = 1;
                            }else {
                                return_value = 0;
                            }
                        }
                    }else {
                        return_value = -1;
                    }
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                if (request_DatabaseManager !=null) {
                    if (-1 == request_DatabaseManager.closeDatabase()) {
                        log.error("Execute function closeDatabase is error");
                        return_value = -1;
                    }
                }
            } finally {                
                request_DatabaseManager = null;
            }
        }
        return return_value;
    }
    
    public static ArrayList<String> getVirusApkList(int STaskReqID) {
        ArrayList<String> virusApkList = new ArrayList<String>();
        DatabaseManager request_DatabaseManager = ConnectDB(host, databaseNameRequest, databaseUserName, databaseUserPassword);
        if (request_DatabaseManager == null) {
            System.err.println("Execute function connectDatabase is error");
            log.error("Execute function connectDatabase is error");
            return null;
        }
//        select  A.FilePath , B.MD5, B.Engine, B.VirusName  from mesdb.Tbl_STaskSampleQueue as A, mesdb.Tbl_STaskScanResult as B 
//        where B.STaskReqID = 57 and A.MD5=B.MD5;
        try{
            if (1 == request_DatabaseManager.checkTableExist(Tbl_STaskRequestLog)) {
                String selectSql = "select  A.FilePath from mesdb.Tbl_STaskSampleQueue as A, mesdb.Tbl_STaskScanResult as B " +
                		"where A.MD5=B.MD5 and B.STaskReqID=" +
                		STaskReqID + //"57" +
                		" and A.STaskReqID=" +
                		STaskReqID +
                		" ;";
                ResultSet rs_virus = request_DatabaseManager.executeQuery(selectSql);
                while (rs_virus.next()) {
                    virusApkList.add(rs_virus.getString("FilePath"));
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                if (request_DatabaseManager !=null) {
                    if (-1 == request_DatabaseManager.closeDatabase()) {
                        log.error("Execute function closeDatabase is error");
                    }
                }
            } finally {                
                request_DatabaseManager = null;
            }
        }
        return virusApkList;
    }
    
    /**
     * 
     * @param host
     * @param databaseName
     * @param databaseUserName
     * @param databaseUserPassword
     * @param tableName
     * @return
     */
    
    public static DatabaseManager ConnectDB(String host, String databaseName, String databaseUserName, String databaseUserPassword){
        if (databaseName == null || databaseUserName == null
                || databaseUserPassword == null   ) {
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
                log.debug("host_bk databaseManager.connectDatabase()  "
                        + re);
                return null;
        }
        return databaseManager;
    }
    /**
     * 
     * @param databaseManager
     */
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
    
    
    
    
    
    
    
    

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
