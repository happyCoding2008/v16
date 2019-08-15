package com.qf.threadlocal.dao;

import com.qf.threadlocal.Connection;
import com.qf.threadlocal.util.ConnectionUtils;

/**
 * @author huangguizhao
 */
public class Dao1 {

    public void add(){
        System.out.println("Dao1 add");
        Connection connection = ConnectionUtils.getConnection();
        System.out.println("dao1:"+connection);
    }
}
