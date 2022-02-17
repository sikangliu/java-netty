package com.lsk.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {

    public static void main(String[] args) {

        //buf -> byte[10]           unlooped -》 非池化
        ByteBuf buf = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            //如果整数没有超过byte就会自己转换
            buf.writeByte(i);
        }
        //在这个buf中不需要flip进行反转
        // （底层维护了一个 readerIndex 和 writerIndex 和 capacity ）
        // 将buffer分为三个区 0 - readerIndex - writerIndex - capacity
        for (int i = 0; i < buf.capacity(); i++) {
            // 直接获取buf位置的值输出，readerIndex的值不会改变
            System.out.println(buf.getByte(i));
        }
        for (int i = 0; i < buf.capacity(); i++) {
            // 使用buf自己的方法readByte, readerIndex会改变
            System.out.println(buf.readByte());
        }

        buf.readerIndex();
    }
}
