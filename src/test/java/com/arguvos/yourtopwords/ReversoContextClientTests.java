package com.arguvos.yourtopwords;

import com.arguvos.yourtopwords.reversocontext.ReversoContextClient;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReversoContextClientTests {

    @Test
    public void getTranslations() throws Exception {
        ReversoContextClient reversoContextClient = new ReversoContextClient("en", "ru");
        List<String> translations = reversoContextClient.getTranslations("hello");
        assert !translations.isEmpty();
    }
}
