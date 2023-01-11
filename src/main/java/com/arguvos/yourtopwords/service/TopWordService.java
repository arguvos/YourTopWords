package com.arguvos.yourtopwords.service;

import com.arguvos.yourtopwords.model.State;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopWordService {
    public State next(State state, Boolean isKnow) {
        String nextWord = TopWords.TOP_1000.get(0);
        if (state.getCurrentWord() != null) {
            int index = TopWords.TOP_1000.indexOf(state.getCurrentWord());

            if (isKnow != null && isKnow) {
                state.getWordStatistics()[index] = true;
            }

            nextWord = TopWords.TOP_1000.get(index + 1 >= TopWords.TOP_1000.size() ? index : index + 1);
        }
        return new State(nextWord, state.getWordStatistics());
    }


    public State prev(State state) {
        String prevWord = TopWords.TOP_1000.get(0);
        if (state.getCurrentWord() != null) {
            int index = TopWords.TOP_1000.indexOf(state.getCurrentWord());
            prevWord = TopWords.TOP_1000.get(Math.max(index - 1, 0));
        }
        return new State(prevWord, state.getWordStatistics());
    }

    public List<String> getUnKnowWords(boolean[] unKnowWords) {
        List<String> unKnowWordString = new ArrayList<>();
        for (int i = 0; i < unKnowWords.length; i++) {
            if (!unKnowWords[i]) {
                unKnowWordString.add(TopWords.TOP_1000.get(i));
            }
        }
        return unKnowWordString;
    }
}
