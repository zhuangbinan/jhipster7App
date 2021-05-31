package com.mycompany.myapp.utils;

import io.netty.buffer.ByteBuf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestMain {
    public static void main(String[] args) {
//        TCPClient client = new TCPClient();
        ByteBuf byteBuf = null;
        try {
            TCPClient.sandMsg("1234","192.168.1.112",4195);
//            TCPClient.sandMsg("hello , wo shi java client","127.0.0.1",4195);
            System.out.println("已发送");
//
//            IntBuffer intBuffer = IntBuffer.allocate(5);
//            IntBuffer flip = intBuffer.flip();
//            NIOChannel03();

//            char x = '张';
//            System.out.println(x);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void NIOChannel03(){
        try{
            FileInputStream fileInputStream = new FileInputStream("1.txt");

            FileChannel inputFileChannel = fileInputStream.getChannel();

            FileOutputStream fileOutputStream = new FileOutputStream("2.txt");

            FileChannel outFileChannel = fileOutputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            while (true) {

                byteBuffer.clear();
                int read = inputFileChannel.read(byteBuffer);
                if (read == -1) {
                    break;
                }
                System.out.println(new String(byteBuffer.array()));
                byteBuffer.flip();
                outFileChannel.write(byteBuffer);
            }
            fileInputStream.close();
            fileOutputStream.close();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
