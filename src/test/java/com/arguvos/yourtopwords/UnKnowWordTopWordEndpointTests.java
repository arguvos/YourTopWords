package com.arguvos.yourtopwords;


import com.arguvos.yourtopwords.service.TopWordLoader;
import com.arguvos.yourtopwords.util.EncodeHelper;
import com.arguvos.yourtopwords.util.TestHelper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class UnKnowWordTopWordEndpointTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TopWordLoader topWordLoader;

    @Test
    void dontKnowWordNext() throws Exception {
        int currentIndex = 2;
        String currentWord = topWordLoader.getTopWords().get(currentIndex);

        this.mockMvc
                .perform(post(TestHelper.UNKNOWWORDS_URL_TEMPLATE)
                        .cookie(new Cookie(TestHelper.CURRENT_WORD, currentWord))
                        .cookie(new Cookie(TestHelper.WORD_STATISTICS, EncodeHelper.encode(TestHelper.EMPTY_WORD_STATISTIC)))
                        .param(TestHelper.IS_KNOW, Boolean.FALSE.toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(TestHelper.ROOT_EXPRESSION).isArray());
    }

}
