package com.akbar.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * Redis配置类
 * 其实springboot已经自动配置好了
 * 这里进行配置主要是为了Key序列化器的设置
 */
@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始配置Redis...");

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 配置连接工厂，其实springboot已经自动配置好了
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置key的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 让spring做最后的其它初始化操作
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
