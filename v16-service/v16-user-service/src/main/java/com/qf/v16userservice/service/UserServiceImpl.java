package com.qf.v16userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.api.IUserService;
import com.qf.v16.common.base.BaseServiceImpl;
import com.qf.v16.common.base.IBaseDao;
import com.qf.v16.common.pojo.ResultBean;
import com.qf.v16.entity.TUser;
import com.qf.v16.mapper.TUserMapper;
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
            //登录验证成功！
            //1，生成唯一标识uuid
            String uuid = UUID.randomUUID().toString();
            //2，将一对key-value保存到redis中，并且设置有效期为30分钟
            //usertoken:uuid---------userin
            StringBuilder key = new StringBuilder("usertoken:").append(uuid);
            //为安全性，将密码去掉
            currentUser.setPassword(null);
            //3,保存到Redis中
            redisTemplate.opsForValue().set(key.toString(),currentUser);
            redisTemplate.expire(key.toString(),30, TimeUnit.MINUTES);
            //4.返回结果
            return new ResultBean("200",uuid);
        }
        //登录验证失败！
        return new ResultBean("404","账号或密码错误！");
    }

    @Override
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
    }

    @Override
    public ResultBean logout(String uuid) {
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
