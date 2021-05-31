package com.mycompany.myapp.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClientTest {

    public static void main(String[] args) throws Exception {


        start();

    }

    @SuppressWarnings("all")
    public static void start() throws Exception{

        //客户端和服务器端不一样, 客户端只需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();
        //创建客户端配置对象
        //服务器端使用ServerBootStrap
        //客户端用BootStrap
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                .channel(NioSocketChannel.class) //设置客户端通道的实现类
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyClientHandler());//加入处理器handler
                    }
                });
            System.out.println("client is ok...");

            //启动客户端去连接服务器端
//            ChannelFuture cf1 = bootstrap.connect("127.0.0.1", 10000).sync();
            ChannelFuture cf1 = bootstrap.connect("127.0.0.1", 10002).sync();
            ChannelFuture cf2 = bootstrap.connect("127.0.0.1", 10003).sync();


//            bootstrap.remoteAddress("127.0.0.1",10003);

            //给关闭通道事件进行一个监听
            cf1.channel().closeFuture().sync();
            cf2.channel().closeFuture().sync();
        }finally {
            //优雅的关闭
            group.shutdownGracefully();
        }
    }

}
