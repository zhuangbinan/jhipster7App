package com.mycompany.myapp.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOChannel04 {
    public static void main(String[] args) {
        try {
            NIOChannel04();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void NIOChannel04(){
        try{
            FileInputStream fileInputStream = new FileInputStream("2.txt");
            FileOutputStream fileOutputStream = new FileOutputStream("3.txt");

            FileChannel srcCh = fileInputStream.getChannel();
            FileChannel desCh = fileOutputStream.getChannel();

            desCh.transferFrom(srcCh,0,srcCh.size());

            srcCh.close();
            desCh.close();

            fileInputStream.close();
            fileOutputStream.close();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
