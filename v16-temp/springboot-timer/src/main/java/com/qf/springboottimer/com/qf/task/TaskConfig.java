package com.qf.springboottimer.com.qf.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * @author huangguizhao
 */
@Configuration
public class TaskConfig {

    @Scheduled(cron = "0/5 * * * * ?")
    public void run(){
        System.out.println("run...."+new Date());
    }
}
