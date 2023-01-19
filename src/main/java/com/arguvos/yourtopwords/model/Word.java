package com.arguvos.yourtopwords.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Word {
    private String word;
    private int number;
    private boolean finish;
}
