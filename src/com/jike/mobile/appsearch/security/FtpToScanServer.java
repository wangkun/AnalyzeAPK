
package com.jike.mobile.appsearch.security;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FtpToScanServer {

    // server：服务器名字
    // user：用户名
    // password：密码
    // path：服务器上的路径
    /**
     * Description: 向FTP服务器上传文件
     * 
     * @param url FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param path FTP服务器保存目录
     * @param filename 上传到FTP服务器上的文件名
     * @param input 输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String url, int port, String username, String password,
            String path, String filename, InputStream input) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(url, port);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.changeWorkingDirectory(path);
            ftp.storeFile(filename, input);

            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }

    public static void main(String[] args) {
        // ftp： 58.68.224.155 user： admin
        // password: root@wanAMAAS
        // directory: ftp://192.168.1.2/file

        try {
            String user = "admin";
            String path = "file/testapk";
            FileInputStream input;
            input = new FileInputStream(new File("D://getApkBasicInfo.jar"));
            String password = "root@wanAMAAS";
            String filename = "/home/ftp2/admin/file/testapk";
            String url = "58.68.224.155";
            int port = 21;
            boolean re=uploadFile(url, port, user, password, path, filename, input);
            System.out.println(re + " @ "  );
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
