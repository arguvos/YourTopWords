package com.arguvos.yourtopwords.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class TopWordLoader {
    private static final String TOP_WORD_FILENAME = "topEngWords.txt";
    @Value("${top.word.file.name:#{null}}")
    private String topWordFileName;
    public List<String> topWords = new ArrayList<>();
    private boolean loaded = false;

    @PostConstruct
    private void init() throws IOException, URISyntaxException {
        if (topWordFileName == null) {
            topWords = Files.readAllLines(Paths.get(getClass().getClassLoader().getResource(TOP_WORD_FILENAME).toURI()));
        } else {
            topWords = Files.readAllLines(Paths.get(topWordFileName));
        }
        loaded = true;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public List<String> getTopWords() {
        return topWords;
    }
}
