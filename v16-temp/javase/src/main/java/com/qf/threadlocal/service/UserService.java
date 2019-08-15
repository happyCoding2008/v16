package com.qf.threadlocal.service;

import com.qf.threadlocal.Connection;
import com.qf.threadlocal.dao.Dao1;
import com.qf.threadlocal.dao.Dao2;

/**
 * @author huangguizhao
 */
public class UserService {

    private Dao1 dao1 = new Dao1();
    private Dao2 dao2 = new Dao2();

    /**
     * 事务的边界是放在业务层
     * JDBC 基于connection来控制事务
     * 所以dao1跟dao2必须用同一个connection
     */
    public void add(){
        dao1.add();
        dao2.add();
    }
}
