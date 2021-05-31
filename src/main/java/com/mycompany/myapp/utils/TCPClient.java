package com.mycompany.myapp.utils;


import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 主要用于发送数据，用完关闭
 */
public class TCPClient {

    public static void sandMsg(String msg, String ipAddr, int port) throws IOException {
        //创建Socket对象
        Socket s = new Socket(InetAddress.getByName(ipAddr), port);
        //获取输出流对象
        OutputStream os = s.getOutputStream();
        //发送数据
        byte[] bytes = DataConvertUtil.hexStr2ByteArray(msg);
        os.write(bytes);
        os.flush();
        os.close();
        //释放
        s.close();
    }
    @Async
    public  void sandMsgAsyn(String msg, String ipAddr, int port) throws IOException {
            //创建Socket对象
            Socket s = new Socket(InetAddress.getByName(ipAddr), port);
            //获取输出流对象
            OutputStream os = s.getOutputStream();
            //发送数据
            byte[] bytes = DataConvertUtil.hexStr2ByteArray(msg);
            os.write(bytes);
            os.flush();
            os.close();
            //释放
            s.close();
    }
}
