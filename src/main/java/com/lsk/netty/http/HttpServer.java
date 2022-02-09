package com.lsk.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class HttpServer {
    public static void main(String[] args) throws Exception {
        //BossGroup and workerGroup
        //创建两个线程组，都是循环
        //bossgroup 和 workergroup 含有子线程（EventLoop）的个数
        EventLoopGroup bossgroup = new NioEventLoopGroup();     //处理连接请求，其余交给workergroup处理
        EventLoopGroup workergroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossgroup, workergroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpInitializer());// 添加自定义初始化

            ChannelFuture channelFuture = serverBootstrap.bind(8081).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossgroup.shutdownGracefully();
            workergroup.shutdownGracefully();
        }
    }
}
