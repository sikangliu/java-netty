package com.lsk.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        //读写时都需要指定编码
        ByteBuf buf = Unpooled.copiedBuffer("hello,world", CharsetUtil.UTF_8);

        byte[] array = buf.array();// 转数组

        System.out.println(new String(array, Charset.forName("utf-8")));// 数组转字符串

        System.out.println("buf 对象 " + buf);// 对象

        System.out.println(buf.arrayOffset());// 第一个字节位置
        System.out.println(buf.readerIndex());// 读的位置，只有readByte方法可以影响
        System.out.println(buf.writerIndex());// 写的位置，进入buf就影响位置
        System.out.println(buf.capacity());// buf容量

        System.out.println(buf.getByte(0));// buf中第一个字节（ascell）

        System.out.println(buf.readableBytes());// 可读的字节长度

        // 循环遍历
//        for (int i = 0; i < buf.readableBytes(); i++) {
//            System.out.println((char) buf.getByte(i));
//        }

        // 从buf的某段中取
        System.out.println(buf.getCharSequence(0, 4, Charset.forName("utf-8")));


    }
}
