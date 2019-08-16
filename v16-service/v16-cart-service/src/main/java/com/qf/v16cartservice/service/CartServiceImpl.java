package com.qf.v16cartservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v16.api.ICartService;
import com.qf.v16.common.pojo.ResultBean;
import com.qf.v16.pojo.CartItem;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author huangguizhao
 */
@Service
public class CartServiceImpl implements ICartService{

    @Resource(name = "redisTemplate4String")
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public ResultBean add(String key, Long productId, Integer count) {
        //1.获取购物车信息
        StringBuilder redisKey = new StringBuilder("user_cart:").append(key);
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(redisKey.toString());
        //2.当前添加的商品是否已经存在购物车中
        if(cart.get(productId) != null){
            //3.如果存在，则更新数量
            CartItem cartItem = (CartItem) cart.get(productId);
            cartItem.setCount(cartItem.getCount()+count);
            //更新操作时间
            cartItem.setUpdateTime(new Date());

            //重新写到Redis中
            redisTemplate.opsForHash().putAll(redisKey.toString(),cart);
            return new ResultBean("200","添加成功！");
        }
        //4.如果不存在，则直接添加到购物车中
        CartItem cartItem = new CartItem(productId,count,new Date());
        cart.put(productId,cartItem);
        //重新写到Redis中
        redisTemplate.opsForHash().putAll(redisKey.toString(),cart);
        return new ResultBean("200","添加成功！");
    }

    @Override
    public ResultBean del(String key, Long productId) {
        //1.获取购物车信息
        StringBuilder redisKey = new StringBuilder("user_cart:").append(key);
        //2.直接删除
        Long delete = redisTemplate.opsForHash().delete(redisKey.toString(), productId);
        if(delete > 0){
            return new ResultBean("200","删除成功！");
        }
        return new ResultBean("404","删除失败");
    }

    @Override
    public ResultBean update(String key, Long productId, Integer count) {
        //1.获取购物车信息
        StringBuilder redisKey = new StringBuilder("user_cart:").append(key);
        //2.获取购物车中的对象信息
        CartItem cartItem =
                (CartItem) redisTemplate.opsForHash().get(redisKey.toString(), productId);
        if(cartItem != null){
            //3.更新其购买数量
            cartItem.setCount(count);
            cartItem.setUpdateTime(new Date());
            //4.重新写回redis
            redisTemplate.opsForHash().put(redisKey.toString(),productId,cartItem);
            return new ResultBean("200","更新成功!");
        }
        return new ResultBean("404","不存在该商品，更新失败！");
    }

    @Override
    public ResultBean query(String key) {
        //1.获取购物车信息
        StringBuilder redisKey = new StringBuilder("user_cart:").append(key);
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(redisKey.toString());
        if(cart.size() > 0){
            //做排序处理
            Collection<Object> values = cart.values();
            //设置比较规则
            TreeSet<CartItem> target = new TreeSet<>();
            //将购物车的信息逐个添加到新集合中，自动根据排序规则进行排序
            for (Object value : values) {
                target.add((CartItem) value);
            }
            return new ResultBean("200",target);
        }
        return new ResultBean("404","当前购物车为空");
    }

    @Override
    public ResultBean merge(String noLoginKey, String loginKey) {
        //for for
        return null;
    }
}
