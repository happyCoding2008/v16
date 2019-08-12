package com.qf.rabbitmq.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huangguizhao
 * 消费者，用于接收消息，进行处理
 */
public class MyConsumer1 {

    private static final String EXCHANGE_NAME = "direct-exchange";
    private static final String QUEUE_NAME = "direct-exchange-queue1";

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
        //ADD 声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //ADD 将队列绑定到交换机上
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"nba");
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"yule");
        //3.消息者来处理消息
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                System.out.println("消费者1接收到的消息为："+message);
            }
        };
        //4.消费者去监听队列
        channel.basicConsume(QUEUE_NAME,true,consumer);
    }
}
