package com.qf.jedis;

import com.qf.entity.Student;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author huangguizhao
 */
public class JedisTest {

    @Test
    public void stringTest(){
        //1.建立跟Redis服务器的连接
        Jedis jedis = new Jedis("192.168.142.137",6379);
        //设置密码
        jedis.auth("java1904");
        //2.操作string
        jedis.set("target","jedis");
        String target = jedis.get("target");
        System.out.println(target);

        Student student = new Student();
        //student->json
        //json->student
        //jedis.set("student",student);
    }

    @Test
    public void hashTest(){
        //1.建立跟Redis服务器的连接
        Jedis jedis = new Jedis("192.168.142.137",6379);
        //设置密码
        jedis.auth("java1904");
        //2.
        Map<String, String> map = new HashMap<>();
        map.put("name","开一间咖啡屋");
        map.put("price","999");
        jedis.hmset("book:1",map);
        //
        Map<String, String> book = jedis.hgetAll("book:1");
        Set<Map.Entry<String, String>> entries = book.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey()+"->"+entry.getValue());
        }
    }

    @Test
    public void listTest(){
        //1.建立跟Redis服务器的连接
        Jedis jedis = new Jedis("192.168.142.137",6379);
        //设置密码
        jedis.auth("java1904");
        //2.
        jedis.lpush("targets","jedis","springdata-redis","springboot-redis");
        List<String> targets = jedis.lrange("targets", 0, -1);
        for (String target : targets) {
            System.out.println(target);
        }
    }
}
