package com.jike.mobile.appsearch.server;

import com.jike.mobile.appsearch.serviceImple.GetApkInfosSerivceImpl;
import com.jike.mobile.appsearch.thirft.GetApkInfo;
import com.jike.mobile.appsearch.util.Constants;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

public class GetApkInfosServiceServer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length==1) {
            Constants.CONFIG_FILE_PATH=args[0];
        }
//        else 本地路径
      //设置端口
        
        TServerSocket serverSocket;
        try {
            serverSocket = new TServerSocket(7911);
            Factory profFactory = new TBinaryProtocol.Factory();
            @SuppressWarnings({
                    "rawtypes", "unchecked"
            })
            TProcessor processor = new GetApkInfo.Processor(new GetApkInfosSerivceImpl());
            Args args1 = new Args(serverSocket);
            args1.processor(processor);
            args1.protocolFactory(profFactory);
            TServer server = new TThreadPoolServer(args1);
            server.serve();
        } catch (TTransportException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        
//        GetApkInfosServiceServer server = new GetApkInfosServiceServer();
//        server.start();
    }
    
//    @SuppressWarnings({ "rawtypes", "unchecked" })  
//    private void start() {  
//        try {  
//            TNonblockingServerSocket socket = new TNonblockingServerSocket(7911);  
//            final GetApkInfo.Processor processor = new GetApkInfo.Processor(new GetApkInfosSerivceImpl());  
//            THsHaServer.Args arg = new THsHaServer.Args(socket);  
//            // 高效率的、密集的二进制编码格式进行数据传输  
//            // 使用非阻塞方式，按块的大小进行传输，类似于 Java 中的 NIO  
//            arg.protocolFactory(new TBinaryProtocol.Factory());  
//            arg.transportFactory(new TFramedTransport.Factory());  
//            arg.processorFactory(new TProcessorFactory(processor));  
//            TServer server = new THsHaServer(arg);  
//            server.serve();  
//            System.out.println("#服务启动-使用:非阻塞&高效二进制编码");  
//        } catch (TTransportException e) {  
//            e.printStackTrace();  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
//    } 

}
