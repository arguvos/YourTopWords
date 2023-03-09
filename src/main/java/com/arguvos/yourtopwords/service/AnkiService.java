package com.arguvos.yourtopwords.service;

import com.anki.tool4j.Anki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class AnkiService {
    private static final String ANKI_PATH = "anki";
    @Autowired
    TranslateCache translateCache;

    public String createAnkiFile(List<String> unKnowWords, String targetLanguage) {
        Anki anki = new Anki(UUID.randomUUID().toString());
        unKnowWords.forEach(unKnowWord -> {
            TranslateCache.Translation translate = translateCache.getTranslate(new TranslateCache.WordKey(targetLanguage, unKnowWord));
            if (translate != null && translate.getTranslate() != null && !translate.getTranslate().isEmpty()) {
                anki.addItem(unKnowWord, translate.getTranslate().get(0));
            }
        });
        File file = new File(ANKI_PATH);
        anki.createApkgFile(file.getAbsolutePath());
        return ANKI_PATH + "/" + anki.getName() + ".apkg";
    }
}
