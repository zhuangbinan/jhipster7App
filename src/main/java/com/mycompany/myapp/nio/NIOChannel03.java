package com.mycompany.myapp.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOChannel03 {
    public static void main(String[] args) {
        try {
            NIOChannel03();

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
