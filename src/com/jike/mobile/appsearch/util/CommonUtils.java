
package com.jike.mobile.appsearch.util;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

public class CommonUtils {
    private static final Logger log = LogManager.getLogger(CommonUtils.class);

    public static void deleteFiles(File file) {
//        System.gc();
        if (file==null) {
            return;
        }
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFiles(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            if(file.exists()){
                file.delete();
                Runtime runtime=Runtime.getRuntime();
            }
        } else {
            System.out.println("所删除的文件已经不存在！" + '\n');
        }
        // System.out.println(file.getAbsolutePath()+" 删除完毕!" );
    }
    
    
    public static void deletFileByExec(String filePath) {
        Runtime runtime=Runtime.getRuntime();
        String command="rm -rf "+filePath;
        System.gc();
        log.debug(command);
        try {
            
            Thread.sleep(100);
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
    * try to delete given file , try 10 times
    * @param f
    * @return true if file deleted success, nor false;
    */
    public static boolean forceDelete(File f) {
        boolean result = false;
        int tryCount = 0;
        while (!result && tryCount++ < 10) {
            log.debug("try to delete file " + f.getName() + " cnt:" + tryCount);
            System.gc();
            result = f.delete();
        }
        return result;
    }


    public static String StrArrayListToString(ArrayList<String> strArrayList) {
        String reString = "";
        for (String str : strArrayList) {
            reString = reString + str + ",";
        }
        return reString;
    }

    public static void copyFile(File src, File dest) {
        int byteRead = 0;
        int byteSum = 0;
        File mFile = dest;
        try {
            if (mFile.exists()) {
                System.out.println("文件存在");
            } else {
                System.out.println("文件不存在，正在创建...");
                if (mFile.createNewFile()) {
                    System.out.println("文件创建成功！");
                } else {
                    System.out.println("文件创建失败！");
                }
            }

            FileOutputStream outputStream = new FileOutputStream(mFile);
            InputStream input = FileUtils.openInputStream(src);

            byte[] buffer = new byte[1024];
            while ((byteRead = input.read(buffer)) != -1) {
                byteSum += byteRead;
                System.out.println("byteSum: " + byteSum);
                outputStream.write(buffer);
            }
            input.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static File WriteByteBufferToFile(ByteBuffer bbuf, String iconPath) {
        File iconFile = new File(iconPath);
        // Set to true if the bytes should be appended to the file;
        // set to false if the bytes should replace current bytes
        // (if the file exists)
        boolean append = false;
        try {
            // Create a writable file channel
            FileChannel wChannel = new FileOutputStream(iconFile, append).getChannel();
            // Write the ByteBuffer contents; the bytes between the ByteBuffer's
            // position and the limit is written to the file
            wChannel.write(bbuf);
            // Close the file
            wChannel.close();
        } catch (IOException e) {
        }
        return iconFile;
    }
    public static HashMap<String, String> getPropertiesValueMap(String properties){
        HashMap<String, String> hm = new HashMap<String, String>();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(properties));
            ResourceBundle bundle = new PropertyResourceBundle(in);
            Set<String> e = bundle.keySet();
            Iterator<String> keys = e.iterator();
            while(keys.hasNext()) {
                String key = keys.next();
                hm.put(key, bundle.getString(key));
            }
        } catch (FileNotFoundException e) {
            log.error("Can not find properties to configurate...");
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hm;
    }
    public static ArrayList<String> getBlackList(){
        ArrayList<String> blackList = new ArrayList<String>();
//        12630595494585610995
        blackList.add("7563004401684491839");
        blackList.add("52448422673114159");
        blackList.add("12630595494585610995");
        
        return blackList;
    }
    public static String getPropertiesValue(String name,String proFilePath) {
//        String proFilePath = "PrivacyInfo.properties";
        InputStream in = null;
        String value = null;
        try {
            in = new BufferedInputStream(new FileInputStream(proFilePath));
            ResourceBundle bundle = new PropertyResourceBundle(in);
            value = bundle.getString(name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        in = null;
        return value;
    }
    static String str = "[android.permission.WRITE_EXTERNAL_STORAGE, com.android.launcher.permission.INSTALL_SHORTCUT, android.permission.INTERNET, android.permission.ACCESS_FINE_LOCATION, android.permission.ACCESS_COARSE_LOCATION, android.permission.CALL_PHONE, android.permission.SEND_SMS, android.permission.VIBRATE, android.permission.ACCESS_WIFI_STATE, android.permission.CHANGE_WIFI_STATE, android.permission.READ_PHONE_STATE, android.permission.PERSISTENT_ACTIVITY, android.permission.RESTART_PACKAGES, android.permission.GET_TASKS, android.permission.ACCESS_NETWORK_STATE, android.permission.RECORD_AUDIO, android.permission.ACCESS_FINE_LOCATION, android.permission.INTERNET, android.permission.ACCESS_COARSE_LOCATION, android.permission.WRITE_EXTERNAL_STORAGE, android.permission.WRITE_EXTERNAL_STORAGE, com.android.launcher.permission.INSTALL_SHORTCUT, android.permission.INTERNET, android.permission.ACCESS_FINE_LOCATION, android.permission.ACCESS_COARSE_LOCATION, android.permission.CALL_PHONE, android.permission.SEND_SMS, android.permission.VIBRATE, android.permission.ACCESS_WIFI_STATE, android.permission.CHANGE_WIFI_STATE, android.permission.READ_PHONE_STATE, android.permission.PERSISTENT_ACTIVITY, android.permission.RESTART_PACKAGES, android.permission.GET_TASKS, android.permission.ACCESS_NETWORK_STATE, android.permission.RECORD_AUDIO]";
    public static ArrayList<String> getArrayListFromString(String str){
        ArrayList<String> strArrayList = new ArrayList<String>();
        if (str.length()>4) {
            str = str.substring(1, str.length()-1);
            strArrayList.addAll(Arrays.asList(str.split(", ")));
        }
        return strArrayList;
    }
    
    public static void main(String[] args) {
        System.out.println(getArrayListFromString(str).toString());
    }
    public static InputStream ByteBufferToInputStream(final ByteBuffer buf) {
//        CommonUtils.WriteByteBufferToFile(buf, "ByteBufferToInputStream.png");
        return new InputStream() {
            public synchronized int read() throws IOException {
                if (!buf.hasRemaining()) {
                    return -1;
                }
                return buf.get();
            }

            public synchronized int read(byte[] bytes, int off, int len) throws IOException {
                if (!buf.hasRemaining()) {
                return -1;
                }
                // Read only what's left
                len = Math.min(len, buf.remaining());
                buf.get(bytes, off, len);
                return len;
                }
        };
    }
    
    public static long getApkMakeTime(String apkPath){
        ZipFile zFile;
        try {
            zFile = new ZipFile(apkPath);
            ZipEntry entry = zFile.getEntry("AndroidManifest.xml"); 
            long time = entry.getTime();//
            entry=null;
            zFile.close();
            zFile=null;
            
            return time;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    
}
