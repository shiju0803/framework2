package com.shiju.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author shiju
 * @date 2021/06/17 16:21
 */
@Configuration
public class RedisConfig {

    // @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        //设置序列化器代替默认的序列化器
        template.setDefaultSerializer(new StringRedisSerializer());
        return template;
    }

}
