package com.solomon.websocketdemo.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public class UdpUtil {
    public static void main(String[] args) {
        System.out.println("hello");
        //定义一个端口号
        int port = 10086;
        try {
            //创建接收方的套接字,监听端口号
            DatagramSocket getSocket = new DatagramSocket(port);
            while(true){
                //确定接收的数据报文的长度，来建立缓冲区
                byte[] buf = new byte[36];
                //创建接收类型的数据包，数据先储存在缓冲区
                DatagramPacket getPacket = new DatagramPacket(buf,buf.length);
                //通过套接字接收数据
                getSocket.receive(getPacket);
                //解析接收到到16机制数据
                byte[] bytes = getPacket.getData();
                String data = getBufHexStr(bytes);
                System.out.println("data:"+data);//接收成功后回复
                backHeadle(getSocket,getPacket);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //回复数据
    private static void backHeadle(DatagramSocket getSocket, DatagramPacket getPacket) {
        //通过数据包得到发送方的套接字ip
        SocketAddress sendAddress = getPacket.getSocketAddress();
        //确定要回复给发送方的消息内容，并转换成字符数组
        String feedback = "211502a0";
        //由于16进制字符发送时只能发送字节，这里讲字符串转换成字节
        byte bytes[] = getHexBytes(feedback);
        //创建发送类型的数据包
        DatagramPacket sendPacket = new DatagramPacket(bytes,bytes.length,sendAddress);
        //通过套接字发送数据
        try {
            getSocket.send(sendPacket);
            System.out.println("发送成功");
        } catch (IOException e) {
            System.out.println("发送失败");
            e.printStackTrace();
        }
    }

    //将16进制的byte数组转换成字符串
    public static String getBufHexStr(byte[] raw){
        String HEXES = "0123456789ABCDEF";
        if ( raw == null ) {
            return null;
        }
        final StringBuilder hex = new StringBuilder( 2 * raw.length );
        for ( final byte b : raw ) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    //将16进制的字符串转成字符数组
    public static byte[] getHexBytes(String str){
        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }
}
