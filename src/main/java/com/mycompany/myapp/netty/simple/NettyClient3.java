package com.mycompany.myapp.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient3 {

    public static void main(String[] args) throws Exception {

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
            System.out.println("客户单 is ok...");

            //启动客户端去连接服务器端
//            ChannelFuture cf = bootstrap.connect("127.0.0.1", 6668);
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 10003);
            cf.sync();
            //给关闭通道事件进行一个监听
            cf.channel().closeFuture().sync();
        }finally {
            //优雅的关闭
            group.shutdownGracefully();
        }

    }

}
