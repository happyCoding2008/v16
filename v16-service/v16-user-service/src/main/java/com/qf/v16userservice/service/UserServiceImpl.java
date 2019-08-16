package com.qf.v16userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.api.IUserService;
import com.qf.v16.common.base.BaseServiceImpl;
import com.qf.v16.common.base.IBaseDao;
import com.qf.v16.common.pojo.ResultBean;
import com.qf.v16.entity.TUser;
import com.qf.v16.mapper.TUserMapper;
import com.qf.v16userservice.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService{

    @Autowired
    private TUserMapper userMapper;

    @Resource(name = "redisTemplate4String")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public ResultBean checkLogin(TUser user) {
        //1.根据用户名查看用户信息（用户名是唯一）
        TUser currentUser = userMapper.selectByUsername(user.getUsername());
        //2.做密码的匹配判断
        if(user.getPassword().equals(currentUser.getPassword())){
            //登录验证成功！ redis---》JWT
            //1.生成令牌
            JwtUtils jwtUtils = new JwtUtils();
            jwtUtils.setTtl(30*60*1000);
            jwtUtils.setSecretKey("java1904");

            String jwtToken = jwtUtils.createJwtToken(currentUser.getId().toString(), currentUser.getUsername());
            //2.返回结果
            return new ResultBean("200",jwtToken);
        }
        //登录验证失败！
        return new ResultBean("404","账号或密码错误！");
    }

    /*@Override
    public ResultBean checkIsLogin(String uuid) {
        //1.根据获取到的uuid，组成一个key
        StringBuilder key = new StringBuilder("usertoken:").append(uuid);
        //2.查询redis中，是否存在对应的记录
        TUser currentUser = (TUser) redisTemplate.opsForValue().get(key.toString());
        //3.如果存在，则表示处于登录状态
        if(currentUser != null){
            //4.刷新凭证的有效期
            redisTemplate.expire(key.toString(),30,TimeUnit.MINUTES);
            //5.返回结果
            return new ResultBean("200",uuid);
        }
        //5.如果不存在，则表示未登录状态
        return new ResultBean("404","当前用户未登录！");
    }*/

    @Override
    public ResultBean checkIsLogin(String jwtToken) {
        //1.解析令牌
        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.setSecretKey("java1904");

        try {
            Claims claims = jwtUtils.parseJwtToken(jwtToken);
            //获取到用户的信息
            String username = claims.getSubject();
            //TODO 刷新令牌的有效期
            return  new ResultBean("200",jwtToken);
        }catch (SignatureException e){
            //出现异常，则表示令牌有问题或者令牌已过期
            return new ResultBean("404","当前用户未登录！");
        }
    }

    @Override
    public ResultBean logout(String uuid) {
        //如果是采用令牌的方式，服务端无需做任何操作，因为令牌只保存在客户端，而服务端只是对令牌的合法性做认证
        //1.根据获取到的uuid，组成一个key
        StringBuilder key = new StringBuilder("usertoken:").append(uuid);
        //2.删除凭证信息
        redisTemplate.delete(key.toString());
        return new ResultBean("200","已删除凭证信息！");
    }

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }
}
