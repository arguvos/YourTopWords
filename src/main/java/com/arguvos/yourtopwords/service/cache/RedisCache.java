package com.arguvos.yourtopwords.service.cache;

import com.arguvos.yourtopwords.service.TranslateCache;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCache implements Cache {
    HashOperations<TranslateCache.WordKey, Integer, TranslateCache.Translation> cache;
    public RedisCache(RedisTemplate<TranslateCache.WordKey, TranslateCache.Translation> redisTemplateRole) {
        cache = redisTemplateRole.opsForHash();
    }

    @Override
    public void put(TranslateCache.WordKey key, TranslateCache.Translation value) {
        cache.put(key, key.hashCode(), value);
    }

    @Override
    public TranslateCache.Translation get(TranslateCache.WordKey key) {
        return cache.get(key, key.hashCode());
    }

    @Override
    public boolean contains(TranslateCache.WordKey key) {
        return cache.hasKey(key, key.hashCode());
    }
}
