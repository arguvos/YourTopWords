package com.arguvos.yourtopwords.service.cache;

import com.arguvos.yourtopwords.service.TranslateCache;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MemCache implements Cache {
    @Value("${memcache.hostname:127.0.0.1}")
    private String hostname;
    @Value("${memcache.port:11211}")
    private int port;
    private static MemcachedClient memcachedClient;

    public MemCache() {
        try {
            memcachedClient = new MemcachedClient(new InetSocketAddress(hostname, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(TranslateCache.WordKey key, TranslateCache.Translation value) {
        memcachedClient.set(key.toString(), -1, value);
    }

    @Override
    public TranslateCache.Translation get(TranslateCache.WordKey key) {
        return (TranslateCache.Translation) memcachedClient.get(key.toString());
    }

    @Override
    public boolean contains(TranslateCache.WordKey key) {
        return  memcachedClient.get(key.toString()) != null;
    }
}