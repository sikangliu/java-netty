package com.lsk.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatSever {
    //监听端口
    private final int port;

    public GroupChatSever(int port) {
        this.port = port;
    }

    //处理客户端请求
    public void run() throws InterruptedException {
        // 创建 bossGroup，workerGroup 用于处理事件
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 服务端设置ServerBootstrap对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            //链式编程，设置参数
            bootstrap.group(bossGroup, workerGroup)// 添加 bossGroup，workerGroup
                    .channel(NioServerSocketChannel.class)// 添加服务端channel
                    .option(ChannelOption.SO_BACKLOG, 128)// 设置服务端channel可以支持多少个请求
                    .childOption(ChannelOption.SO_KEEPALIVE, true)// 设置服务端channel保持连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {// 设置请求channel处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {// 初始化方法添加设置
                            //获取pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());//向pipeline里加入一个解码器（handler）
                            pipeline.addLast("encoder", new StringEncoder());//向pipeline里加入一个编码器（handler）
                            //加入自定义的业务handler，来处理请求的channel
                            pipeline.addLast(new GroupChatServerHandler());
                        }
                    });
            System.out.println("netty 服务器启动...");
            //绑定端口
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            //监听关闭事件，有关闭事件了就监听到
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        GroupChatSever groupChatSever = new GroupChatSever(8081);
        groupChatSever.run();
    }
}
