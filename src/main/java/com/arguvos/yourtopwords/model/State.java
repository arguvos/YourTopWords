package com.arguvos.yourtopwords.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class State {
    private String currentWord;
    private boolean[] wordStatistics;
}
