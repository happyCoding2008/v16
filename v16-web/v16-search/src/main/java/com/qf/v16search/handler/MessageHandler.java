package com.qf.v16search.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.api.ISearchService;
import com.qf.v16.common.constant.RabbitMQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author huangguizhao
 * 处理消息
 */
@Component
public class MessageHandler {

    //注入service实现
    @Reference
    private ISearchService searchService;
    //
    @RabbitListener(queues = RabbitMQConstant.ADDORUPDATE_SEARCH_QUEUE)
    @RabbitHandler
    public void processAdd(Long id){
        System.out.println(id);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        searchService.updateById(id);
    }
}
