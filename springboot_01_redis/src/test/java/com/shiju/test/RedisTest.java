package com.shiju.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author shiju
 * @date 2021/06/17 16:06
 */
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testSet() {
        //存入数据，底层调用的还是opsForValue().set(key,value)方法
        redisTemplate.opsForValue().set("name", "huang");
        // redisTemplate.boundValueOps("name").set("黄桂田");
    }

    @Test
    public void testGet() {
        //取出数据，底层调用的还是opsForValue().get(key)方法
        // Object name = redisTemplate.boundValueOps("name").get();
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name =" + name);
    }
}
