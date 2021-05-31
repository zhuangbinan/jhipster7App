package com.mycompany.myapp.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGatheringTest {


    public static void main(String[] args) throws Exception{

        //使用ServerSocketChannel 和 SocketChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //创建Buffer数组,并且给定值
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接 (telnet)
        SocketChannel socketChannel = serverSocketChannel.bind(inetSocketAddress).accept();

        long msgLength = 8;

        //循环的读取
        while(true){

            int byteRead = 0; //计数, 计量读了几个字节数

            while(byteRead < msgLength){ // 当 计量到的字节数  <  总发的消息数时 Loop
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead="+byteRead);

                //使用打印流,看看当前的这个buffer的position和limit
                Arrays.asList(byteBuffers)
                    .stream()
                    .map(buffer
                            -> "position = " + buffer.position() +
                               ",  limit = " + buffer.limit())
                    .forEach(System.out::println);
            }

            //将所有的buffer进行 flip
            //因为读到了 很可能还要输出 , 就是读完了可能还要写,就要反转
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            //将数据读出 显示到 客户端
            long byteWrite = 0 ;
            while (byteWrite < msgLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            //将所有的buffer进行clear()操作
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
            System.out.println("byteRead= "+byteRead +
                            ", byteWrite= "+byteWrite+"" +
                            ", msgLength= "+msgLength);

        }

    }



}
