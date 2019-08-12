package com.qf.rabbitmqspringboot.rabbitmq;

import com.qf.rabbitmqspringboot.common.CommonConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangguizhao
 */
@Configuration
public class RabbitmqConfig {

    @Bean
    public Queue initQueue(){
        return new Queue(CommonConstant.QUEUE_NAME,false,false,false);
    }
}
