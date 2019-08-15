package com.qf.threadlocal;

import java.util.Random;

/**
 * @author huangguizhao
 */
public class ThreadLocalTest {

    private static ThreadLocal<Double> threadLocal = new ThreadLocal<>();

    public static void main(String[] args){
        new Thread(new Task()).start();
        new Thread(new Task()).start();
    }

    static class Task implements Runnable{
        @Override
        public void run() {
            Double random = Math.random();
            System.out.println(Thread.currentThread().getName()+"->"+random);
            threadLocal.set(random);
            System.out.println(Thread.currentThread().getName()+"->"+threadLocal.get());
        }
    }
}
