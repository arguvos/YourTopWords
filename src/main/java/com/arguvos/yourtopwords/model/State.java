package com.arguvos.yourtopwords.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class State {
    private String currentWord;
    private int currentWordNumber;
    private boolean[] wordStatistics;

    public State(String currentWord, boolean[] wordStatistics) {
        this.currentWord = currentWord;
        this.wordStatistics = wordStatistics;
    }
}
