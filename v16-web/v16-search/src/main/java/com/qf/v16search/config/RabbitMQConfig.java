package com.qf.v16search.config;

import com.qf.v16.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangguizhao
 */
@Configuration
public class RabbitMQConfig {

    //1.声明队列
    @Bean
    public Queue initQueue(){
        return new Queue(RabbitMQConstant.ADDORUPDATE_SEARCH_QUEUE,
                false,false,false);
    }

    //2.将队列绑定到交换机上
    @Bean
    public TopicExchange initTopicExchange(){
        return new TopicExchange(RabbitMQConstant.BACKGROUND_PRODUCT_EXCHANGE,
                true,false);
    }

    @Bean
    public Binding bindExchange(Queue initQueue,TopicExchange initTopicExchange){
        return BindingBuilder.bind(initQueue).to(initTopicExchange).with("product.add");
    }
}
