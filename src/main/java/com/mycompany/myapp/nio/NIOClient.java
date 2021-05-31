package com.mycompany.myapp.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {



    public static void main(String[] args) throws Exception{

        SocketChannel socketChannel = SocketChannel.open();

        boolean connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 6666));
        if (!connect){
            while(!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间,客户端不会阻塞,可以做其他工作..");
            }
        }

        //如果连接成功,就发送数据
        String str = "hello , i am java NIO Client 2 ?";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(buffer);
        System.in.read();


    }

}
