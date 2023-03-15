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
class PrevTopWordEndpointTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private TopWordLoader topWordLoader;

	@Test
	void firstPrev() throws Exception {
		this.mockMvc
				.perform(post(TestHelper.PREV_URL_TEMPLATE))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath(TestHelper.WORD_EXPRESSION).value(topWordLoader.getTopWords().get(0)))
				.andExpect(MockMvcResultMatchers.jsonPath(TestHelper.NUMBER_EXPRESSION).value(1))
				.andExpect(MockMvcResultMatchers.jsonPath(TestHelper.FINISH_EXPRESSION).value(false))
				.andExpect(MockMvcResultMatchers.cookie().value(TestHelper.CURRENT_WORD, topWordLoader.getTopWords().get(0)))
				.andExpect(MockMvcResultMatchers.cookie().value(TestHelper.WORD_STATISTICS, EncodeHelper.encode(TestHelper.EMPTY_WORD_STATISTIC)));
	}

	@Test
	void prev() throws Exception {
		int currentIndex = TestHelper.COUNT_ELEMENT - 1;
		String currentWord = topWordLoader.getTopWords().get(currentIndex);

		String expectedPrevWord = topWordLoader.getTopWords().get(currentIndex - 1);
		int expectedPrevWordNumber = currentIndex;

		this.mockMvc
				.perform(post(TestHelper.PREV_URL_TEMPLATE)
						.cookie(new Cookie(TestHelper.CURRENT_WORD, currentWord))
						.cookie(new Cookie(TestHelper.WORD_STATISTICS, EncodeHelper.encode(TestHelper.EMPTY_WORD_STATISTIC)))
						.param(TestHelper.IS_KNOW, Boolean.FALSE.toString()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath(TestHelper.WORD_EXPRESSION).value(expectedPrevWord))
				.andExpect(MockMvcResultMatchers.jsonPath(TestHelper.NUMBER_EXPRESSION).value(expectedPrevWordNumber))
				.andExpect(MockMvcResultMatchers.jsonPath(TestHelper.FINISH_EXPRESSION).value(false));
	}
}
