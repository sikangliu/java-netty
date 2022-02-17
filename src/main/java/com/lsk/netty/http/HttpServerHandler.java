package com.lsk.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import java.net.URI;

/**
 * SimpleChannelInboundHandler:是 ChannelInboundHandlerAdapter 的子类（通道入栈处理器）
 * HttpObject:客户端和服务端相互通讯的数据被封装成HttpObject
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端事件
    //ctx的真实类型DefaultChannelHandlerContext
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        //判断msg是不是httprequest请求
        if (msg instanceof HttpRequest) {
            System.out.println("msg 类型：" + msg.getClass());
            System.out.println("客户端地址： " + ctx.channel().remoteAddress());
            System.out.println("ctx 的 类型 ： " + ctx.getClass());
            System.out.println("cxt 中的pipelin ： " + ctx.pipeline());//保存全部handler信息
            System.out.println("cxt 中的channel ： " + ctx.channel());
            System.out.println("cxt 中的handler ： " + ctx.handler());

            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.uri());
            //过滤指定资源
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 /favicon.ico ，但不做反应");
                return;
            }
            //回复浏览器信息{http协议}
            ByteBuf content = Unpooled.copiedBuffer("你好，浏览器...", CharsetUtil.UTF_8);
            //构造一个http相应，即response 版本和状态码
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            //将构建好的response返回
            ctx.writeAndFlush(response);
        }
    }
}
