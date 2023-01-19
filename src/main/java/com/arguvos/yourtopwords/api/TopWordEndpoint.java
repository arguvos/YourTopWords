package com.arguvos.yourtopwords.api;

import com.arguvos.yourtopwords.model.Word;
import com.arguvos.yourtopwords.model.State;
import com.arguvos.yourtopwords.service.TopWordService;
import com.arguvos.yourtopwords.service.TopWords;
import com.arguvos.yourtopwords.util.EncodeHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("top1000")
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:3000")
public class TopWordEndpoint {

    @Autowired
    TopWordService topWordService;

    @PostMapping("/next")
    public ResponseEntity<Object> next(@CookieValue(name = "currentWord", required = false) String currentWord,
                                       @CookieValue(name = "wordStatistics", required = false) String wordStatistics,
                                       Boolean isKnow,
                                       HttpServletResponse response) {
        State state = new State(currentWord, EncodeHelper.decodeOrCreate(wordStatistics, TopWords.TOP_1000.size()));

        State nextState = topWordService.next(state, isKnow);

        response.addCookie(new Cookie("currentWord", nextState.getCurrentWord()));
        response.addCookie(new Cookie("wordStatistics", EncodeHelper.encode(nextState.getWordStatistics())));
        boolean isFinish = currentWord != null &&  currentWord.equals(nextState.getCurrentWord());
        return new ResponseEntity<>(new Word(nextState.getCurrentWord(), nextState.getCurrentWordNumber(), isFinish), HttpStatus.OK);
    }

    @PostMapping("/prev")
    public ResponseEntity<Object> prev(@CookieValue(name = "currentWord", required = false) String currentWord,
                                       @CookieValue(name = "wordStatistics", required = false) String wordStatistics,
                                       HttpServletResponse response) {
        if (currentWord == null) {
            currentWord = TopWords.TOP_1000.get(0);
        }

        State state = new State(currentWord, EncodeHelper.decodeOrCreate(wordStatistics, TopWords.TOP_1000.size()));

        State prevState = topWordService.prev(state);

        response.addCookie(new Cookie("currentWord", prevState.getCurrentWord()));
        response.addCookie(new Cookie("wordStatistics", EncodeHelper.encode(prevState.getWordStatistics())));
        return new ResponseEntity<>(new Word(prevState.getCurrentWord(), prevState.getCurrentWordNumber(), false), HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<Object> reset(HttpServletResponse response) {
        response.addCookie(new Cookie("currentWord", null));
        response.addCookie(new Cookie("wordStatistics", null));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/unknowwords")
    public ResponseEntity<Object> unknowwords(@CookieValue(name = "wordStatistics") String wordStatistics) {
        return new ResponseEntity<>(topWordService.getUnKnowWords(EncodeHelper.decode(wordStatistics)), HttpStatus.OK);
    }
}