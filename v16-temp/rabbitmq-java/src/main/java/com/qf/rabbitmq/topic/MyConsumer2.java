package com.qf.rabbitmq.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huangguizhao
 * 消费者，用于接收消息，进行处理
 */
public class MyConsumer2 {

    private static final String EXCHANGE_NAME = "topic-exchange";
    private static final String QUEUE_NAME = "topic-exchange-queue2";

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
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"product.#");

        //ADD 限制服务器每次给消费端发送的消息数量
        //限流
        channel.basicQos(1);

        //3.消息者来处理消息
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                //模拟消耗一秒钟处理完消息
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("消费者2接收到的消息为："+message);
                //ADD 处理完毕之后，回复一个确认消息给MQ服务器
                //false 仅确认当前这个消息
                //true 批量确认 返回所有的消息，这些消息的标志是比当前这个小的或者等于的情况
                //6 1-6
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //4.消费者去监听队列
        //ADD 自动回复模式修改为手工确认模式
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
