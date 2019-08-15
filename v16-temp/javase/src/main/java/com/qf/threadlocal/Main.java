package com.qf.threadlocal;

import com.qf.threadlocal.service.UserService;

/**
 * @author huangguizhao
 */
public class Main {
    public static void main(String[] args){
        UserService userService = new UserService();
        userService.add();
    }
}
