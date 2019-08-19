package com.qf.v16cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v16.api.ICartService;
import com.qf.v16.common.pojo.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author huangguizhao
 */
@RestController
@RequestMapping("cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @RequestMapping("add/{productId}/{count}")
    public ResultBean add(@PathVariable("productId") Long productId,
                          @PathVariable("count") Integer count,
                          @CookieValue(name = "user_cart",required = false) String uuid,
                          HttpServletResponse response,
                          HttpServletRequest request){

        //1.获取当前用户的状态
        String userId = (String) request.getAttribute("userId");
        if(userId != null){
            //TODO 发送异步消息，更新数据库的数据
            //已登录
            return cartService.add(userId,productId,count);
        }

        //未登录
        //1.判断是否为第一次操作购物车
        if(uuid == null || "".equals(uuid)){
            //1.创建uuid
            uuid = UUID.randomUUID().toString();
            //2.写cookie
            flushCookie(uuid, response,30*24*60*60);
        }
        //2.调用服务，添加商品到购物车
        return cartService.add(uuid,productId,count);
    }

    @RequestMapping("query")
    public ResultBean query(@CookieValue(name = "user_cart",required = false) String uuid,
                            HttpServletResponse response,
                            HttpServletRequest request){

        //1.获取当前用户的状态
        String userId = (String) request.getAttribute("userId");
        if(userId != null){
            //已登录
            return cartService.query(userId);
        }

        //未登录
        //1.判断是否为第一次操作购物车
        if(uuid == null || "".equals(uuid)){
            return new ResultBean("404","您的购物车已经被老公清空了！");
        }
        //更新cookie的有效期
        flushCookie(uuid,response,30*24*60*60);
        //2.调用服务，查看我的购物车
        return cartService.query(uuid);
    }

    @RequestMapping("update/{productId}/{count}")
    public ResultBean update(@PathVariable("productId") Long productId,
                             @PathVariable("count") Integer count,
                             @CookieValue(name = "user_cart",required = false) String uuid,
                             HttpServletResponse response){
        //1.判断是否为第一次操作购物车
        if(uuid == null || "".equals(uuid)){
            return new ResultBean("404","您的购物车已经被老公清空了！");
        }
        //更新cookie的有效期
        flushCookie(uuid,response,30*24*60*60);
        //2.更新购物车
        return cartService.update(uuid,productId,count);
    }

    @RequestMapping("del/{productId}")
    public ResultBean del(@PathVariable("productId") Long productId,
                          @CookieValue(name = "user_cart",required = false) String uuid,
                          HttpServletResponse response){
        //1.判断是否为第一次操作购物车
        if(uuid == null || "".equals(uuid)){
            return new ResultBean("404","您的购物车已经被老公清空了！");
        }
        //更新cookie的有效期
        flushCookie(uuid,response,30*24*60*60);
        //2.更新购物车
        return cartService.del(uuid,productId);
    }

    @RequestMapping("merge")
    public ResultBean merge(@CookieValue(name = "user_cart",required = false) String uuid,
                          HttpServletResponse response,
                          HttpServletRequest request){
        //这个接口只会在登录之后，才会被调用
        //1.获取当前用户的状态
        String userId = (String) request.getAttribute("userId");
        if(userId != null){
            //清楚掉cookie
            flushCookie(uuid,response,0);
            //TODO 发送异步消息，更新数据库的数据
            return cartService.merge(uuid,userId);
        }
        return new ResultBean("404","无需合并");
    }

    private void flushCookie(@CookieValue(name = "user_cart", required = false) String uuid, HttpServletResponse response,int expire) {
        Cookie cookie = new Cookie("user_cart",uuid);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expire);
        cookie.setDomain("qf.com");
        //3.写到客户端
        response.addCookie(cookie);
    }

}
