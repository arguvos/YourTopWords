package com.arguvos.yourtopwords.service.cache;

import com.arguvos.yourtopwords.service.TranslateCache;

import java.util.concurrent.ConcurrentHashMap;

public class MapCache implements Cache {
    private final ConcurrentHashMap<TranslateCache.WordKey, TranslateCache.Translation> cache = new ConcurrentHashMap<>();

    @Override
    public void put(TranslateCache.WordKey key, TranslateCache.Translation value) {
        cache.put(key, value);
    }

    @Override
    public TranslateCache.Translation get(TranslateCache.WordKey key) {
        return cache.get(key);
    }

    @Override
    public boolean contains(TranslateCache.WordKey key) {
        return cache.containsKey(key);
    }
}
