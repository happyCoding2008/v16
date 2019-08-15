package com.qf.threadlocal.util;

import com.qf.threadlocal.Connection;

/**
 * @author huangguizhao
 */
public class ConnectionUtils {

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public static Connection getConnection(){
        //return new Connection();
        Connection connection = threadLocal.get();
        if(connection == null){
            connection = new Connection();
            threadLocal.set(connection);
        }
        return connection;
    }
}
