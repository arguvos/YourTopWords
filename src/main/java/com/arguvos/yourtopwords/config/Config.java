package com.arguvos.yourtopwords.config;

import com.arguvos.yourtopwords.service.TranslateCache;
import com.arguvos.yourtopwords.service.cache.Cache;
import com.arguvos.yourtopwords.service.cache.MapCache;
import com.arguvos.yourtopwords.service.cache.RedisCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class Config {
    @Value("${redis.cache.enable}")
    private boolean redisCacheEnable;
    private final RedisConnectionFactory redisConnectionFactory;

    public Config(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<TranslateCache.WordKey, TranslateCache.Translation> redisTemplate() {
        RedisTemplate<TranslateCache.WordKey, TranslateCache.Translation> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public Cache getMyService() {
        if (redisCacheEnable)
            return new RedisCache(redisTemplate());
        return new MapCache();
    }
}