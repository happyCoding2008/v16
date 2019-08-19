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
        //1.获取未登录购物车
        StringBuilder noLoginRedisKey = new StringBuilder("user_cart:").append(noLoginKey);
        Map<Object, Object> noLoginCart = redisTemplate.opsForHash().entries(noLoginRedisKey.toString());
        if(noLoginCart.size() == 0){
            return new ResultBean("200","不存在未登录购物车，无需合并！");
        }

        //2.获取已登录购物车
        StringBuilder loginRedisKey = new StringBuilder("user_cart:").append(loginKey);
        Map<Object, Object> loginCart = redisTemplate.opsForHash().entries(loginRedisKey.toString());

        if(loginCart.size() == 0){
            //不存在已登录购物车
            //将未登录购物车作为已登录购物车存放进去即可
            redisTemplate.opsForHash().putAll(loginRedisKey.toString(),noLoginCart);
            //删除未登录购物车
            redisTemplate.delete(noLoginRedisKey.toString());
            //
            return new ResultBean("200","不存在已登录购物车，直接将原先的未登录转换为已登录即可！");
        }

        //3.两辆购物车都存在,才需要做真正意义的合并
        //noLoginCart--->loginCart
        Set<Map.Entry<Object, Object>> noLoginEntries = noLoginCart.entrySet();
        //
        for (Map.Entry<Object, Object> noLoginEntry : noLoginEntries) {
            //noLoginEntry.getKey()
            if(redisTemplate.opsForHash().get(loginRedisKey.toString(),noLoginEntry.getKey()) != null){
                //存在，则需要修改数量
                CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(
                        loginRedisKey.toString(), noLoginEntry.getKey());
                //
                CartItem noLoginCartItem = (CartItem) noLoginEntry.getValue();
                cartItem.setCount(cartItem.getCount()+noLoginCartItem.getCount());
                cartItem.setUpdateTime(new Date());
                //重新写回购物车
                redisTemplate.opsForHash().put(loginRedisKey.toString(),noLoginEntry.getKey(),cartItem);
            }else{
                //不存在，则直接添加
                redisTemplate.opsForHash().put(loginRedisKey.toString(),noLoginEntry.getKey(),noLoginEntry.getValue());
            }
        }

        //最后清理掉未登录购物车
        redisTemplate.delete(noLoginRedisKey.toString());

        return new ResultBean("200","合并购物车成功！");
    }
}
