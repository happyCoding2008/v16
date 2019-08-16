package com.qf.v16sso.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.api.IUserService;
import com.qf.v16.common.pojo.ResultBean;
import com.qf.v16.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Reference
    private IUserService userService;

    @RequestMapping("login")
    public String showLogin(){
        return "login";
    }

    @RequestMapping("checkLogin4PC")
    public String checkLogin4PC(String username, String password,
                                         HttpServletResponse response){
        //1.获取到账号和密码
        TUser user = new TUser(username,password);
        //2.调用服务，判断账号是否正确
        ResultBean<String> resultBean = userService.checkLogin(user);
        //3.如果当前账号正确，则写cookie
        if("200".equals(resultBean.getStatusCode())){
            //3.构建cookie user_token-----uuid
            String key = "user_token";
            Cookie cookie = new Cookie(key,resultBean.getData());
            //默认是会话级cookie，不需要设置有效期
            cookie.setPath("/");
            //防止XSS攻击，设置为true，表示客户端无法通过脚本获取到我们的cookie信息
            cookie.setHttpOnly(true);

            //4.将cookie写到浏览器
            response.addCookie(cookie);

            //5.跳转到首页
            return "redirect:http://localhost:9091/index/home";
        }
        //6.跳转回登录页面
        return "login";
    }

    @RequestMapping("checkLogin")
    @ResponseBody
    public ResultBean<String> checkLogin(TUser user,
                                         HttpServletResponse response){

        //1.获取到账号和密码
        //TUser user = new TUser(username,password);
        //2.调用服务，判断账号是否正确
        ResultBean<String> resultBean = userService.checkLogin(user);
        //3.如果当前账号正确，则写cookie
        if("200".equals(resultBean.getStatusCode())){
            //3.构建cookie user_token-----uuid
            String key = "user_token";
            Cookie cookie = new Cookie(key,resultBean.getData());
            //默认是会话级cookie，不需要设置有效期
            cookie.setPath("/");
            //防止XSS攻击，设置为true，表示客户端无法通过脚本获取到我们的cookie信息
            cookie.setHttpOnly(true);
            //为了实现同父域下面的所有子域名网站都可以共享该cookie，需要设置cookie的domain
            cookie.setDomain("qf.com");

            //4.将cookie写到浏览器
            response.addCookie(cookie);
        }
        return resultBean;
    }

    @RequestMapping("checkIsLogin4JSONP")
    @ResponseBody
    public String checkIsLogin4JSONP(@CookieValue(name = "user_token",required = false) String uuid,
                                     String callback) throws JsonProcessingException {

        ResultBean resultBean = null;
        //1.获取到客户端的uuid
        if(uuid != null){
            //2.调用服务来判断登录状态
            resultBean = userService.checkIsLogin(uuid);
        }else{
            resultBean = new ResultBean("404","未登录");
        }
        //2.返回信息
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultBean);
        return callback+"("+json+")";
    }

    @RequestMapping("checkIsLogin")
    @ResponseBody
    @CrossOrigin(origins = "*",allowCredentials = "true")
    public ResultBean checkIsLogin(@CookieValue(name = "user_token",required = false) String uuid){
        //1.获取到客户端的uuid
        if(uuid != null){
            //2.调用服务来判断登录状态
            ResultBean resultBean = userService.checkIsLogin(uuid);
            if("200".equals(resultBean.getStatusCode())){
                //跳转到相关页面
                return resultBean;
            }
        }
        return new ResultBean("404","未登录！");
    }

    @RequestMapping("logout")
    @ResponseBody
    public ResultBean logout(@CookieValue(name = "user_token",required = false) String uuid,
                             HttpServletResponse response){
        if(uuid != null){
            //1.获取uuid
            //2.调用服务，删除保存到redis中的凭证信息
            //ResultBean resultBean = userService.logout(uuid);
            //3.删除客户端的cookie,设置该cookie的有效期为0
            Cookie cookie = new Cookie("user_token",uuid);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);

            response.addCookie(cookie);

            return new ResultBean("200","注销成功！");
        }

        return new ResultBean("404","当前未登录！");
    }

    /*@RequestMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(HttpServletRequest request){
        //1.获取到客户端的uuid
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if("user_token".equals(cookie.getName())){
                    String uuid = cookie.getValue();
                    //2.调用服务来判断登录状态
                    ResultBean resultBean = userService.checkIsLogin(uuid);
                    if("200".equals(resultBean.getStatusCode())){
                        //跳转到相关页面
                        return resultBean;
                    }
                }
            }
        }
        return new ResultBean("404","未登录！");
    }*/
}
