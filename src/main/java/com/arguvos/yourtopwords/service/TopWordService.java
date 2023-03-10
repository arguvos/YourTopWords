package com.arguvos.yourtopwords.service;

import com.arguvos.yourtopwords.model.State;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopWordService {
    public State next(State state, Boolean isKnow) {
        String nextWord = TopWords.TOP_1000.get(0);
        if (state.getCurrentWord() != null && !state.getCurrentWord().isBlank()) {
            int index = TopWords.TOP_1000.indexOf(state.getCurrentWord());

            state.getWordStatistics()[index] = isKnow != null && isKnow;

            nextWord = TopWords.TOP_1000.get(index + 1 >= TopWords.TOP_1000.size() ? index : index + 1);
        }
        return new State(nextWord, TopWords.TOP_1000.indexOf(nextWord) + 1, state.getWordStatistics());
    }


    public State prev(State state) {
        String prevWord = TopWords.TOP_1000.get(0);
        if (state.getCurrentWord() != null) {
            int index = TopWords.TOP_1000.indexOf(state.getCurrentWord());
            prevWord = TopWords.TOP_1000.get(Math.max(index - 1, 0));
        }
        return new State(prevWord, TopWords.TOP_1000.indexOf(prevWord) + 1, state.getWordStatistics());
    }

    public List<String> getUnknowWords(boolean[] unknowWords) {
        List<String> unknowWordList = new ArrayList<>();
        for (int i = 0; i < unknowWords.length; i++) {
            if (!unknowWords[i]) {
                unknowWordList.add(TopWords.TOP_1000.get(i));
            }
        }
        return unknowWordList;
    }
}
