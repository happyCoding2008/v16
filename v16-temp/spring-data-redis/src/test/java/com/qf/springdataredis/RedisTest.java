package com.qf.springdataredis;

import com.qf.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author huangguizhao
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:redis.xml")
public class RedisTest {

    //所有的操作都是基于模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void stringTest(){
        //内部做了处理--做了序列化处理
        redisTemplate.opsForValue().set("smallTarget","1000000");
        Object smallTarget = redisTemplate.opsForValue().get("smallTarget");
        System.out.println(smallTarget);//1000000

        //redisTemplate.delete("smallTarget");
        //操作无差异

        Student student = new Student(1L,"lisi");
        redisTemplate.opsForValue().set("student",student);
        Object student1 = redisTemplate.opsForValue().get("student");
        System.out.println(student1);
    }

    @Test
    public void hashTest(){
        Map<String,String> map = new HashMap<>();
        map.put("name","把时间浪费在美好的事物上");
        map.put("price","9999");
        redisTemplate.opsForHash().putAll("book:2",map);
        //
        Map entries = redisTemplate.opsForHash().entries("book:2");
        Set<Map.Entry<String,String>> set = entries.entrySet();
        for (Map.Entry<String, String> entry : set) {
            System.out.println(entry.getKey()+"->"+entry.getValue());
        }
    }

    @Test
    public void incrTest(){
        //value默认的序列化方式是JDK，而该方式不支持数学运算
        //需要将value的序列化方式设置为String才会支持
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set("money","1000");
        redisTemplate.opsForValue().increment("money",1000);
        System.out.println(redisTemplate.opsForValue().get("money"));
    }

    @Test
    public void nopiplineTest(){
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            redisTemplate.opsForValue().set("k"+i,"v"+i);//2322 18425
        }
        long end = System.currentTimeMillis();
        System.out.println("cost:"+(end-start));
    }

    @Test
    public void piplineTest(){
        long start = System.currentTimeMillis();
        
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                for (int i = 0; i < 100000; i++) {
                    redisTemplate.opsForValue().set("k"+i,"v"+i);//958
                }
                return null;
            }
        });
        
        long end = System.currentTimeMillis();
        System.out.println("cost:"+(end-start));
    }

}
