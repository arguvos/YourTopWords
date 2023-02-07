package com.arguvos.yourtopwords.service;

import com.arguvos.yourtopwords.reversocontext.ReversoContextClient;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class TranslateCache {
    private static final String SOURCE_LANG = "en";
    private static final String TARGET_LANG = "de";
    private static final int DELAY_MS = 10000;
    private static final int SCATTER_MS = 5000;
    private final ConcurrentHashMap<String, Translation> cache = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void run() {
        executor.submit(() -> {
            ReversoContextClient reversoContextClient = new ReversoContextClient(SOURCE_LANG, TARGET_LANG);
            for (String word : TopWords.TOP_1000) {
                log.debug("Loading translation for word {}", word);
                try {
                    List<String> translations = reversoContextClient.getTranslations(word);
                    List<Pair<String, String>> translationSamples = reversoContextClient.getTranslationSamples(word);
                    if (!translations.isEmpty() && !translationSamples.isEmpty()) {
                        Translation translation = new Translation(translations, translationSamples);
                        cache.put(word, translation);
                        log.debug("Loaded translation for word {} : {}", word, translation);
                    } else {
                        log.warn("No translation or samples for word {}", word);
                    }
                } catch (Exception exception) {
                    log.error("Error loading translation for word {}", word, exception);
                }
                sleep();
            }
        });
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
    static class Translation {
        private List<String> translate;
        private List<Pair<String, String>> samples;
    }
}
