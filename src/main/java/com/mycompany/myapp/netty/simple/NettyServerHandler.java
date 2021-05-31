package com.mycompany.myapp.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //当出现异常,关闭通道
        ctx.close();

    }

    //通道读取完毕后,要返回给客户端消息
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello, 我是服务端,我收到了你的消息",CharsetUtil.UTF_8));
    }

    //在这里读取数据实际(这里我们可以读取客户端发送的消息)
    /*
        1.ChannelHandlerContext ctx : 上下文对象,含有管道对象pipeline,通道channel,地址
        2.Object msg: 就是客户端发送的数据

     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("server ctx = "+ ctx);

        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("客户端发来消息 : " + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址 : " + ctx.channel().remoteAddress());
    }


}
