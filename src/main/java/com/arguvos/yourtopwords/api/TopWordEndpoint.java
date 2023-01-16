package com.arguvos.yourtopwords.api;

import com.arguvos.yourtopwords.model.State;
import com.arguvos.yourtopwords.service.TopWords;
import com.arguvos.yourtopwords.util.EncodeHelper;
import com.arguvos.yourtopwords.service.TopWordService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController()
@RequestMapping("top1000")
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:3000")
public class TopWordEndpoint {

    @Autowired
    TopWordService topWordService;

    @PostMapping("/next")
    public String next(@CookieValue(name = "currentWord", required = false) String currentWord,
                       @CookieValue(name = "wordStatistics", required = false) String wordStatistics,
                       Boolean isKnow,
                       HttpServletResponse response) {
        State state = new State(currentWord, EncodeHelper.decodeOrCreate(wordStatistics, TopWords.TOP_1000.size()));

        State nextState = topWordService.next(state, isKnow);

        response.addCookie(new Cookie("currentWord", nextState.getCurrentWord()));
        response.addCookie(new Cookie("wordStatistics", EncodeHelper.encode(nextState.getWordStatistics())));
        return nextState.getCurrentWord();
    }

    @PostMapping("/prev")
    public String prev(@CookieValue(name = "currentWord", required = false) String currentWord,
                       @CookieValue(name = "wordStatistics", required = false) String wordStatistics,
                       HttpServletResponse response) {
        State state = new State(currentWord, EncodeHelper.decodeOrCreate(wordStatistics, TopWords.TOP_1000.size()));

        State prevState = topWordService.prev(state);

        response.addCookie(new Cookie("currentWord", prevState.getCurrentWord()));
        response.addCookie(new Cookie("wordStatistics", EncodeHelper.encode(prevState.getWordStatistics())));
        return prevState.getCurrentWord();
    }

    @PostMapping("/reset")
    public String reset(HttpServletResponse response) {
        response.addCookie(new Cookie("currentWord", null));
        response.addCookie(new Cookie("wordStatistics", null));
        return "ok";
    }

    @PostMapping("/unknowwords")
    public List<String> unknowwords(@CookieValue(name = "wordStatistics") String wordStatistics) {
        return topWordService.getUnKnowWords(EncodeHelper.decode(wordStatistics));
    }
}