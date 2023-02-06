package com.arguvos.yourtopwords;

import com.arguvos.yourtopwords.reversocontext.ReversoContextClient;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReversoContextClientTests {
    private static final String SOURCE_LANG = "en";
    private static final String TARGET_LANG = "de";
    private static final String SOURCE_TEXT = "hello";
    @Test
    public void getTranslations() throws Exception {
        ReversoContextClient reversoContextClient = new ReversoContextClient(SOURCE_LANG, TARGET_LANG);
        List<String> translations = reversoContextClient.getTranslations(SOURCE_TEXT);
        assert !translations.isEmpty();
    }

    @Test
    public void getTranslationSamples() throws Exception {
        ReversoContextClient reversoContextClient = new ReversoContextClient(SOURCE_LANG, TARGET_LANG);
        List<Pair<String, String>> translations = reversoContextClient.getTranslationSamples(SOURCE_TEXT);
        assert !translations.isEmpty();
    }
}
