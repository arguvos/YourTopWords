package com.arguvos.yourtopwords.service;

import com.anki.tool4j.Anki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class AnkiService {
    private static final String SEPARATE = "/";
    private static final String EXTENSION = ".apkg";
    @Value("${anki.dir:anki}")
    private String ankiDir;
    @Autowired
    TranslateCache translateCache;

    public byte[] getAnkiBytes(List<String> unknowWords, String targetLanguage) throws IOException {
        Anki anki = createAnkiFile(unknowWords, targetLanguage);
        Path ankiFilePath = getPath(anki.getName());
        byte[] ankiByte = Files.readAllBytes(ankiFilePath);
        Files.deleteIfExists(ankiFilePath);
        return ankiByte;
    }

    private Anki createAnkiFile(List<String> unKnowWords, String targetLanguage) {
        Anki anki = new Anki(UUID.randomUUID().toString());
        unKnowWords.forEach(unKnowWord -> {
            TranslateCache.Translation translate = translateCache.getTranslate(new TranslateCache.WordKey(targetLanguage, unKnowWord));
            if (translate != null && translate.getTranslate() != null && !translate.getTranslate().isEmpty()) {
                anki.addItem(unKnowWord, translate.getTranslate().get(0));
            }
        });
        File file = new File(ankiDir);
        anki.createApkgFile(file.getAbsolutePath());
        return anki;
    }

    private Path getPath(String name) {
        return Path.of(ankiDir + SEPARATE + name + EXTENSION);
    }
}
