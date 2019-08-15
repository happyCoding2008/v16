package com.qf.springboottimer.com.qf.timer;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author huangguizhao
 */
public class MyTimerTask extends TimerTask {

    @Override
    public void run() {
        System.out.println("run....."+new Date()+Thread.currentThread().getName());
        //1,TODO 查看日志表
        //2.TODO 重新发送邮件
    }
}
