package com.arguvos.yourtopwords.service;

import com.arguvos.yourtopwords.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopWordService {
    private final TopWordLoader topWordLoader;

    @Autowired
    public TopWordService(TopWordLoader topWordLoader) {
        this.topWordLoader = topWordLoader;
    }

    public State next(State state, Boolean isKnow) {
        String nextWord = topWordLoader.getTopWords().get(0);
        if (state.getCurrentWord() != null && !state.getCurrentWord().isBlank()) {
            int index = topWordLoader.getTopWords().indexOf(state.getCurrentWord());

            state.getWordStatistics()[index] = isKnow != null && isKnow;

            nextWord = topWordLoader.getTopWords().get(index + 1 >= topWordLoader.getTopWords().size() ? index : index + 1);
        }
        return new State(nextWord, topWordLoader.getTopWords().indexOf(nextWord) + 1, state.getWordStatistics());
    }


    public State prev(State state) {
        String prevWord = topWordLoader.getTopWords().get(0);
        if (state.getCurrentWord() != null) {
            int index = topWordLoader.getTopWords().indexOf(state.getCurrentWord());
            prevWord = topWordLoader.getTopWords().get(Math.max(index - 1, 0));
        }
        return new State(prevWord, topWordLoader.getTopWords().indexOf(prevWord) + 1, state.getWordStatistics());
    }

    public List<String> getUnknowWords(boolean[] unknowWords) {
        List<String> unknowWordList = new ArrayList<>();
        for (int i = 0; i < unknowWords.length; i++) {
            if (!unknowWords[i]) {
                unknowWordList.add(topWordLoader.getTopWords().get(i));
            }
        }
        return unknowWordList;
    }
}
