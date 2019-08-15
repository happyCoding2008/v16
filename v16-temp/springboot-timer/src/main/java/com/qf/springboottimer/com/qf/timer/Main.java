package com.qf.springboottimer.com.qf.timer;

import java.util.Date;
import java.util.Timer;

/**
 * @author huangguizhao
 */
public class Main {

    public static void main(String[] args){
        MyTimerTask task = new MyTimerTask();
        Timer timer = new Timer();
        System.out.println(new Date());
        //一次性
        //timer.schedule(task,3000);
        //周期性
        timer.schedule(task,3000,1000);
        //15号
    }
}
