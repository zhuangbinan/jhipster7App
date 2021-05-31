package com.mycompany.myapp.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

@SuppressWarnings("all")
public class GroupChatClient2 {

    private static SocketChannel socketChannel;

    private Selector selector;

    private String targetIp = "127.0.0.1";

    private InetSocketAddress socketAddress = new InetSocketAddress(targetIp,6667);

    private String username ;

    public GroupChatClient2() throws IOException {

        selector = Selector.open();
        socketChannel = SocketChannel.open(socketAddress);

        socketChannel.configureBlocking(false);

        this.username = socketChannel.getLocalAddress().toString().substring(1);

        socketChannel.register(selector , SelectionKey.OP_READ );
    }

    //向服务器发送消息
    public void sendInfo(String info) {
        info = "   :  "+ this.username + "说: " + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void readInfo() {
        try {
            int select = selector.select(2000);
            if (select > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {

                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        System.out.println(new String(buffer.array()));
                    }
                    iterator.remove();
                }
            }else {
                System.out.println("没有可用的通道222...");
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        try {
            GroupChatClient2 chatClient = new GroupChatClient2();

            //启动一个线程,每隔3秒,读取从服务器发送的数据
            new Thread(){
                public void run(){

                    while(true){

                        chatClient.readInfo();

                        try {

//                            TimeUnit.SECONDS.sleep(3);
                            Thread.currentThread().sleep(3000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }.start();

            //发送数据给服务器端
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {

                String s = scanner.nextLine();

                chatClient.sendInfo(s);

            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
