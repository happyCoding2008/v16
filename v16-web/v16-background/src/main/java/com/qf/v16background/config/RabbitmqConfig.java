package com.qf.v16background.config;

import com.qf.v16.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author huangguizhao
 */
@Component
public class RabbitmqConfig {

    //topic
    @Bean
    public TopicExchange initTopicExchange(){
        return new TopicExchange(RabbitMQConstant.BACKGROUND_PRODUCT_EXCHANGE,true,false);
    }
}
