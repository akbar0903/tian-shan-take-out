package com.akbar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.concurrent.TimeUnit;

//@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Test
    public void testRedisTemplate() {
        // 看看Redis有没有配置成功
        System.out.println(redisTemplate);

        // 操作String类型
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        // hash类型
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        // 操作List类型
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();

        // 操作Set类型
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();

        // 操作ZSet类型
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
    }


    /**
     * 测试String类型
     * 其实我们可以set任意类型的数据，redis会自动序列化，然后用字符串存储
     */
    @Test
    public void testString() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        // 1.set操作
        valueOperations.set("city", "宜昌");
        // 2.get操作
        String city = (String) valueOperations.get("city");
        System.out.println("city: " + city);

        // 3.插入数据的同时设置过期时间，单位秒，相当于setex命令
        valueOperations.set("age", 25, 3, TimeUnit.MINUTES);

        // 4.如果不存在则设置，存在则不设置
        Boolean result1 = valueOperations.setIfAbsent("name", "张三");
        System.out.println(result1);
        Boolean result2 = valueOperations.setIfAbsent("name", "张三");
        System.out.println(result2);

    }


    /**
     * 测试Hash类型
     */
    @Test
    public void testHash() {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        // 1.put操作
        hashOperations.put("user", "name", "张三");
        hashOperations.put("user", "age", 25);

        // 2.get操作
        Object name = hashOperations.get("user", "name");
        System.out.println("name: " + name);

        // 3.entries操作
        System.out.println("user: " + hashOperations.entries("user"));

        // 4.delete操作
        hashOperations.delete("user", "age");
        System.out.println("user: " + hashOperations.entries("user"));

        // 5.获取所有的key
        System.out.println("keys: " + hashOperations.keys("user"));

        // 6.获取所有的value
        System.out.println("values: " + hashOperations.values("user"));

    }


    /**
     * 通用操作
     */
    @Test
    public void testCommon() {
        // 1.判断key是否存在
        Boolean exists = redisTemplate.hasKey("name");
        System.out.println(exists);

        // 2.删除key
        redisTemplate.delete("name");

        // 3.获取key的过期时间
        Long expire = redisTemplate.getExpire("name");
        System.out.println(expire);

        // 4.设置key的过期时间
        redisTemplate.expire("name", 3, TimeUnit.MINUTES);

        // 5.获取key的类型
        String type = String.valueOf(redisTemplate.type("name"));
        System.out.println(type);
    }
}


