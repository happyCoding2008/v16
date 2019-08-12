package com.qf.rabbitmqspringboot.rabbitmq;

import com.qf.rabbitmqspringboot.common.CommonConstant;
import com.qf.rabbitmqspringboot.entity.ProductDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huangguizhao
 * 生产者
 */
@Component
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String message){
        rabbitTemplate.convertAndSend(CommonConstant.QUEUE_NAME,message);
    }

    public void send(ProductDTO productDTO){
        rabbitTemplate.convertAndSend(CommonConstant.QUEUE_NAME,productDTO);
    }
}
