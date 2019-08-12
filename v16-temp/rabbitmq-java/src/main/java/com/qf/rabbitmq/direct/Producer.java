package com.qf.rabbitmq.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huangguizhao
 * 生产者，用于发送消息
 */
public class Producer {

    private static final String EXCHANGE_NAME = "direct-exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.142.137");
        factory.setUsername("java1904");
        factory.setPassword("123");
        factory.setPort(5672);
        factory.setVirtualHost("/java1904");

        Connection connection = factory.newConnection();
        //2.创建一个跟服务器交互的通道
        Channel channel = connection.createChannel();
        //3.声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        //4.发送消息到队列
        String message1 = "NBA:马云买下湖人队";
        channel.basicPublish(EXCHANGE_NAME,"nba",null,message1.getBytes());

        String message2 = "YULE:***又被离婚了！";
        channel.basicPublish(EXCHANGE_NAME,"yule",null,message2.getBytes());

        System.out.println("发送成功！");
    }
}
