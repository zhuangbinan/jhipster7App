package com.mycompany.myapp.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    public static void main(String[] args) {

        // 创建BossGroup 和 WorkerGroup
        // 说明:
        // 1.这俩group创建了,里面都有无限循环的线程
        // 2.bossGroup只是处理连接的请求,真正的和客户端业务处理,会交给 workerGroup 完成
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建服务器端的启动配置对象 ServerBootStrap
            ServerBootstrap bootstrap = new ServerBootstrap();
//        public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup)
            bootstrap.group(bossGroup, workerGroup) //从形参的名字上来看, bossGroup 还是 父线程组呢
                .channel(NioServerSocketChannel.class) //设置NioServerSocketChannel来作为服务器通道的实现
                .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列的连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true) //父线程组设置完了,开始设置 workerGroup
                .childHandler(new ChannelInitializer<SocketChannel>() {  //创建一个通道初始化对象(匿名)anonymous

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline(); //SocketChannel 和 是我
                        pipeline.addLast(new NettyServerHandler()); //添加handler,现在还没有
                    }

                });

            System.out.println(" ... 服务端 is ok ... ");


            //绑定一个端口并且同步 生成一个ChannelFuture 对象
            //启动服务器,并绑定端口
            ChannelFuture cf = bootstrap.bind(6668).sync();

            Channel channel = cf.channel();

            //对通道关闭事件进行监听
            channel.closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

}
