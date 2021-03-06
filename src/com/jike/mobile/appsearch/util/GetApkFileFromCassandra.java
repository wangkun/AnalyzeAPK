package com.jike.mobile.appsearch.util;

import org.apache.cassandra.thrift.AuthenticationException;
import org.apache.cassandra.thrift.AuthenticationRequest;
import org.apache.cassandra.thrift.AuthorizationException;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class GetApkFileFromCassandra {
    private static final Logger log = LogManager.getLogger(GetApkFileFromCassandra.class); 
    
    static String keyspace = "Keyspace1";
    static String host = "localhost";
    static int port = 9710;
    static String columnName = "apk";
    static String family="Standard1";
    static String apksPath=Constants.APKS_PATH;
    
    static String userName = "";
    static String passwd="";
    static String blockNumberName="block_num";

    private static AuthenticationRequest auth_request = new AuthenticationRequest();

    
    
    
    private static void initCassandra() {
        // TODO Auto-generated method stub
        host = Constants.cassandra_host;
        port = Constants.cassandra_port;
        keyspace = Constants.cassandra_keyspace;
        family = Constants.cassandra_col_fam;
        columnName = Constants.cassandra_apk_col;
        
        userName = Constants.cassandra_user;
        passwd = Constants.cassandra_passwd;
        Map<String,String> credentialMap = new HashMap<String, String>();
        credentialMap.put(userName, passwd);
        auth_request.setCredentials(credentialMap);
        
    }
    
//    public static File getApkFile(String key) {
//        log.debug("key="+key);
//        File apkFile=null;
//        initCassandra();
//        TTransport transport = new TSocket(host,port);
//        
//        try {
//            transport.open();
//        } catch (TTransportException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        TProtocol protocol = new TBinaryProtocol(transport);
//        ApkTfsService.Client client = new ApkTfsService.Client(protocol);
//        
//        try {
//            apkFile=writeFile(client.ReadApk(key).getBytes(),apksPath+key+".apk");
//            log.debug("client.ReadApk(key).length="+client.ReadApk(key).length());
//            log.debug("apkFile="+apkFile.getAbsolutePath());
//            transport.close();
//        }  catch (TException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//        return apkFile;
//    }
    public static File getAPK(String key, String apksPath){
        initCassandra();
        File tempDeFiles=new File(apksPath);
        if (!tempDeFiles.exists()||!tempDeFiles.isDirectory()) {
            tempDeFiles.mkdir();
        }
        File apkFile=null;
        String key_user_id = key;
        TTransport tr = new TFramedTransport(new TSocket(host, port));
        TProtocol proto = new TBinaryProtocol(tr);
        try {
            tr.open();
            
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        Cassandra.Client client = new Cassandra.Client(proto);
        try {
            client.set_keyspace(keyspace);
            client.login(auth_request);
            
            ColumnPath blockNumColumnPath = new ColumnPath();
            blockNumColumnPath.setColumn(blockNumberName.getBytes());
            blockNumColumnPath.setColumn_family(family);
            ColumnOrSuperColumn blockColumn=client.get(ByteBuffer.wrap(key_user_id.getBytes()), blockNumColumnPath, ConsistencyLevel.ONE);
            int blockNumbers=Integer.parseInt(new String(blockColumn.getColumn().getValue()));
            System.out.println("name="+new String(blockColumn.getColumn().getName())+" , value="+blockNumbers);
            byte[] allByte = new byte[0];
            for (int i = 0; i < blockNumbers; i++) {
                ColumnPath column_path = new ColumnPath();
                String columnName_No = columnName + "_" + i;
                column_path.setColumn(columnName_No.getBytes());
                column_path.setColumn_family(family);
                ColumnOrSuperColumn column=client.get(ByteBuffer.wrap(key_user_id.getBytes()), column_path, ConsistencyLevel.ONE);
                byte[] data=column.getColumn().getValue();
                if (allByte==null) {
                    allByte=data;
                }else {
                    byte[] lastByte = new byte[allByte.length];
                    System.arraycopy(allByte,0,lastByte,0,allByte.length);
                    allByte = new byte[data.length+allByte.length];
                    System.arraycopy(lastByte,0,allByte,0,lastByte.length);
                    System.arraycopy(data,0,allByte,lastByte.length,data.length);
                }
            }
            apkFile=writeFile(allByte,apksPath+System.currentTimeMillis()+"_"+key_user_id+".apk");
            System.out.println("writeFile "+apkFile.getAbsolutePath());
            log.debug("writeFile "+apkFile.getAbsolutePath());
            tr.close();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (UnavailableException e) {
            e.printStackTrace();
        } catch (TimedOutException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (AuthorizationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apkFile;
    }
    
    public static File getAPKtoFTP(String key, String apksPath){
        initCassandra();
        File tempDeFiles=new File(apksPath);
        if (!tempDeFiles.exists()||!tempDeFiles.isDirectory()) {
            tempDeFiles.mkdir();
        }
        File apkFile=null;
        String key_user_id = key;
        TTransport tr = new TFramedTransport(new TSocket(host, port));
        TProtocol proto = new TBinaryProtocol(tr);
        try {
            tr.open();
            
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        Cassandra.Client client = new Cassandra.Client(proto);
        try {
            client.set_keyspace(keyspace);
            client.login(auth_request);
            ColumnPath blockNumColumnPath = new ColumnPath();
            blockNumColumnPath.setColumn(blockNumberName.getBytes());
            blockNumColumnPath.setColumn_family(family);
            ColumnOrSuperColumn blockColumn=client.get(ByteBuffer.wrap(key_user_id.getBytes()), blockNumColumnPath, ConsistencyLevel.ONE);
            int blockNumbers=Integer.parseInt(new String(blockColumn.getColumn().getValue()));
            System.out.println("name="+new String(blockColumn.getColumn().getName())+" , value="+blockNumbers);
            byte[] allByte = new byte[0];
            for (int i = 0; i < blockNumbers; i++) {
                ColumnPath column_path = new ColumnPath();
                String columnName_No = columnName + "_" + i;
                column_path.setColumn(columnName_No.getBytes());
                column_path.setColumn_family(family);
                ColumnOrSuperColumn column=client.get(ByteBuffer.wrap(key_user_id.getBytes()), column_path, ConsistencyLevel.ONE);
                byte[] data=column.getColumn().getValue();
                if (allByte==null) {
                    allByte=data;
                }else {
                    byte[] lastByte = new byte[allByte.length];
                    System.arraycopy(allByte,0,lastByte,0,allByte.length);
                    allByte = new byte[data.length+allByte.length];
                    System.arraycopy(lastByte,0,allByte,0,lastByte.length);
                    System.arraycopy(data,0,allByte,lastByte.length,data.length);
                }
            }
            apkFile=writeFile(allByte,apksPath+"/"+key_user_id);
            System.out.println("writeFile "+apkFile.getAbsolutePath());
            log.debug("writeFile "+apkFile.getAbsolutePath());
            tr.close();
            
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (UnavailableException e) {
            e.printStackTrace();
        } catch (TimedOutException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (AuthorizationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apkFile;
    }
    
    

    public static File writeFile(byte[] data, String fileName) throws IOException {
        OutputStream out = new FileOutputStream(fileName);
        try {
            out.write(data);
        } finally {
            out.close();
        }
        File apkFile = new File(fileName);
        if (apkFile.exists()&&apkFile.length()>0) {
            return apkFile;
        }
        log.debug("apkFile is Null");
        return apkFile;
    }
    
    // string to file
    public static File stringToFile(String str, String filename) {
        try {
            BufferedReader in = new BufferedReader(new StringReader(str));
            PrintWriter out = new PrintWriter(new FileWriter(filename));
            String s;
            while ((s = in.readLine()) != null) {
                out.println(s);
            }
            out.close();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
        File apkFile = new File(filename);
        if (apkFile.exists()&&apkFile.length()>0) {
            log.debug("apkFile is = "+apkFile.getAbsolutePath());
            return apkFile;
        }
        log.debug("apkFile is Null");
        return apkFile;
    }

    /**
     * @param args
     */
    private static String apk_key = "virus69.csv";//"failedFileKey.log";
    
    public final static LinkedBlockingQueue<String> decodedApps = new LinkedBlockingQueue<String>();
    public static void main(String[] args) {
//        getAPK("a1");7205617736938680212     ,2 block_num
        apksPath="./virus_apks/";
//        getAPK("665254284149278401",apksPath);//error: 13935918978980920564 kill server:52448422673114159 Can't find framework resources for package  2k=10496040029157976478 zip unlock 1627240477302188645  7096166839111425919无效数据  10867632484002919730 没有 manifestxml 8218817529311072819不是zip
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(  
                    new FileInputStream(apk_key)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
  
        try {
            for (String key = br.readLine(); key != null; key = br.readLine()) {  
                decodedApps.add(key); 
                getAPK(key,apksPath);
//                decodedApps.add(key); 
                if (decodedApps.size()>200) {
                    break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
        try {
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
