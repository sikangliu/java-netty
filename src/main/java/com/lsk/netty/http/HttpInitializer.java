package com.lsk.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


public class HttpInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个netty提供的httpServerCodec -》{code and decode}
        //netty提供的处理http的编码解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());        //相当于netty自带的处理器

        //增加一个自定义的处理器handler
        //addlast 把业务处理类（handler）加入到链表(ChannelPipeline)中最后一个位置，有相应的addfirst
        pipeline.addLast("MyHttpServerHandler", new HttpServerHandler());    //自定义的处理器

    }
}
