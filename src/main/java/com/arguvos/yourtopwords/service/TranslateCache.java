package com.arguvos.yourtopwords.service;

import com.arguvos.yourtopwords.reversocontext.ReversoContextClient;
import com.arguvos.yourtopwords.service.cache.Cache;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class TranslateCache {
    private static final String SOURCE_LANG = "en";
    private static final int DELAY_MS = 10000;
    private static final int SCATTER_MS = 5000;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Cache cache;
    @Value("${languages}")
    private String[] languages;
    @Value("${load.translate.at.start:true}")
    private boolean isLoadTranslate;

    public TranslateCache(Cache cache) {
        this.cache = cache;
    }

    @PostConstruct
    private void run() {
        if (!isLoadTranslate) {
            log.info("Load translate at start is disable");
            return;
        }
        executor.submit(() -> {
            for (String language : languages) {
                ReversoContextClient reversoContextClient = new ReversoContextClient(SOURCE_LANG, language);
                for (String word : TopWords.TOP_1000) {
                    log.debug("Loading translation for word \"{}\" and language {}", word, language);
                    WordKey key = new WordKey(language, word);
                    if (cache.contains(key)) {
                        continue;
                    }
                    try {
                        List<String> translations = reversoContextClient.getTranslations(word);
                        List<Pair<String, String>> translationSamples = reversoContextClient.getTranslationSamples(word);
                        if (!translations.isEmpty() && !translationSamples.isEmpty()) {
                            Translation translation = new Translation(translations, translationSamples);
                            cache.put(key, translation);
                            log.debug("Loaded translation for word \"{}\" : {}", word, translation);
                        } else {
                            log.warn("No translation or samples for word \"{}\" and language {}", word, language);
                        }
                    } catch (Exception exception) {
                        log.error("Error loading translation for word \"{}\" and language {}", word, language);
                    }
                    sleep();
                }
            }
        });
    }

    public TranslateCache.Translation getTranslate(WordKey wordKey) {
        return cache.get(wordKey);
    }

    private synchronized static void sleep() {
        Random random = new Random();
        try {
            Thread.sleep(DELAY_MS + random.nextInt(SCATTER_MS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isLoaded() {
        return executor.isShutdown();
    }

    @Data
    @AllArgsConstructor
    public static class WordKey implements Serializable {
        private String language;
        private String word;
    }

    @Data
    @AllArgsConstructor
    public static class Translation implements Serializable {
        private List<String> translate;
        private List<Pair<String, String>> samples;
    }
}
