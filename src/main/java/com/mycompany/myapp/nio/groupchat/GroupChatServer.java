package com.mycompany.myapp.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {

    private static ServerSocketChannel listenChannel;

    private static Selector selector;

    private static int PORT = 6667;

    private static InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);

    public GroupChatServer() throws Exception{

        selector = Selector.open();

        listenChannel = ServerSocketChannel.open();

        listenChannel.socket().bind(inetSocketAddress);

        listenChannel.configureBlocking(false);

        listenChannel.register(selector,SelectionKey.OP_ACCEPT);

        System.out.println("Sever 已启动!");
    }

    public void listen(){
        try{
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector,SelectionKey.OP_READ);

                            System.out.println(sc.getRemoteAddress() + " 连上了");
                        }
//                        读取客户端消息
                        if (key.isReadable()) {
                            //读取客户端消息并告知其他客户端的方法
                            readData(key);
                        }
                        iterator.remove();
                    }

                } else{
                    System.out.println("等待 ... ");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void readData(SelectionKey key){
        SocketChannel channel = null;
        try{
            //1是自己接到消息并显示
            //通过key获取Channel, 要转成SocketChannel, 因为这个类里有getRemoteAddress()和getLocalAddress()
            channel = (SocketChannel) key.channel();
            //创建Buffer对象, 用ByteBuffer,
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //channel读buffer
            int count = channel.read(buffer);
            if (count > 0) {
                String msg = new String(buffer.array());
                System.out.println(" from 客户端:" + channel.getRemoteAddress() + " 发来消息:" + msg);

                //2是消息还要转发给其他客户端,写一个专门转发的方法
                transportToOtherClients(msg,channel);
            }

        }catch (Exception e){
            e.printStackTrace();
            try {
                System.out.println(channel.getRemoteAddress() + "离线了...");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }

    //转发给其他客户端(channel)
    private void transportToOtherClients(String msg ,SocketChannel self) throws IOException{
//        服务器消息转发中
        System.out.println("服务器消息转发中...");

        for (SelectionKey key : selector.keys()) {

            Channel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self){
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg 存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());

                //将buffer的数据写入通道
                dest.write(buffer);
            }

        }
    }


    public static void main(String[] args) throws Exception {
        new GroupChatServer().listen();
    }

}
