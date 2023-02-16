package com.arguvos.yourtopwords.service.cache;

import com.arguvos.yourtopwords.service.TranslateCache;

public interface Cache {

    void put(TranslateCache.WordKey key, TranslateCache.Translation value);

    TranslateCache.Translation get(TranslateCache.WordKey key);

    boolean contains(TranslateCache.WordKey key);
}
