package com.lsk.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;

/**
 * 检测心跳机制
 */
public class MyServer {
    public static void main(String[] args) throws Exception {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    /**
                     * IdleStateHandler: 是netty提供的处理空闲状态的处理器
                     * 参数说明:
                     * 1、readerIdleTime：读心跳时长，表示多长时间没有读，就会发送一个心跳检测包检查是否连接
                     * 2、writerIdleTime：写心跳时长，表示多长时间没有写，就会发送一个心跳检测包检查是否连接
                     * 3、allIdleTime：读写心跳时长，表示多长时间没有读写，就会发送一个心跳检测包检查是否连接
                     *
                     * 当触发IdleStateHandler这个处理器时，会交给管道的下一个Handler去处理，
                     * 通过调用触发下一个处理器的 userEventTriggered方法进行处理，在该方法中处理空闲事件（读空闲、写空闲、读写空闲。。）
                     */
                    pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));

                    // 添加对空闲检测的自定义处理器
                    pipeline.addLast(new MyServerHandler());

                }
            });

            ChannelFuture sync = serverBootstrap.bind(7000).sync();
            sync.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
