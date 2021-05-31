package com.mycompany.myapp.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws Exception{

        //创建ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定一个端口6666,在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置非阻塞
        serverSocketChannel.configureBlocking(false);

        //把 serverSocketChannel 注册到 selector 关心事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true) {
            //这里我们等待1秒,如果没有事件发生,返回
            if (selector.select(1000) == 0){ //没有事件发生
                System.out.println("服务器等待了1秒,无连接");
                continue;
            }

            //如果返回的不是>0,就获取到相关的 selectionKey 集合
            //1.如果返回的>0, 表示已经获取到关注的事件
            //2.selector.selectedKeys() 返回关注事件的集合
            //  通过selectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历这个Set集合
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的事件做相应的处理
                if (key.isAcceptable()) { //如果是 OP_ACCEPT ,有新的客户端连接
                    //给该客户端生成一个SocketChannel;
                    //以事件驱动的, 到这一步说明已经有至少1个客户端来连接了
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功 生成了一个SocketChannel:" + socketChannel.hashCode());
                    //设置非阻塞
                    socketChannel.configureBlocking(false);

                    //将socketChannel也注册到selector上
                    //这里关注的时间为 OP_READ ; 同时给socketChannel关联一个Buffer
                    socketChannel.register(selector,
                        SelectionKey.OP_READ,
                        ByteBuffer.allocate(1024));


                }
                if (key.isReadable()) { //发生了 OP_READ
                    //通过key反向获取对应的Channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    channel.read(buffer);
                    System.out.println("from 客户端 " + new String(buffer.array()));
                }

                //手动从集合中移除当前的SelectionKey,防止重复操作
                keyIterator.remove();

            }


        }



    }

}
