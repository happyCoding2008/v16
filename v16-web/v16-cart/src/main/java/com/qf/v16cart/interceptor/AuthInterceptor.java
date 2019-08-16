package com.qf.v16cart.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.api.IUserService;
import com.qf.v16.common.pojo.ResultBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huangguizhao
 */
@Component
public class AuthInterceptor implements HandlerInterceptor{


    /**
     * Spring给你注入，你必须是归Spring管理的
     */
    @Reference
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //目的：获取当前已登录的用户信息
        //user_token--->jwtToken(id)
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if("user_token".equals(cookie.getName())){
                    String jwtToken = cookie.getValue();
                    //解析jwttoken获取到id信息
                    ResultBean resultBean = userService.parseTokenGetId(jwtToken);
                    if("200".equals(resultBean.getStatusCode())){
                        String userId = (String) resultBean.getData();
                        //将其保存到request中
                        request.setAttribute("userId",userId);
                        return true;
                    }
                }
            }
        }
        //无论是否登录，都可以操作购物车
        return true;
    }
}
