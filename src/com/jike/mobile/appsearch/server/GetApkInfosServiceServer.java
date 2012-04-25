package com.jike.mobile.appsearch.server;

import com.jike.mobile.appsearch.serviceImple.GetApkInfosSerivceImpl;
import com.jike.mobile.appsearch.thirft.GetApkInfo;
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
      //设置端口
        TServerSocket serverSocket;
        try {
            serverSocket = new TServerSocket(7911);
            Factory profFactory = new TBinaryProtocol.Factory();
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
    }

}
