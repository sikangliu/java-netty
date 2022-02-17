package com.lsk.netty.heartbeat;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String stateType = "";
            switch (event.state()) {
                case READER_IDLE:
                    stateType = "读空闲";
                    break;
                case WRITER_IDLE:
                    stateType = "写空闲";
                    break;
                case ALL_IDLE:
                    stateType = "读写空闲";
                    break;
            }

            System.out.println("当前状态：" + stateType);


        }
    }
}
