
package com.jike.mobile.appsearch.util;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

public class CommonUtils {
    private static final Logger log = LogManager.getLogger(CommonUtils.class);

    public static void deleteFiles(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFiles(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            System.out.println("所删除的文件已经不存在！" + '\n');
        }
        // System.out.println(file.getAbsolutePath()+" 删除完毕!" );
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
}
