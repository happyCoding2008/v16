package com.qf.threadlocal.dao;

import com.qf.threadlocal.Connection;
import com.qf.threadlocal.util.ConnectionUtils;

/**
 * @author huangguizhao
 */
public class Dao2 {

    public void add(){
        System.out.println("Dao2 add...");
        Connection connection = ConnectionUtils.getConnection();
        System.out.println("dao2:"+connection);
    }
}
