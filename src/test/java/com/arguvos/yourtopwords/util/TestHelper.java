package com.arguvos.yourtopwords.util;

public class TestHelper {
    public static final int COUNT_ELEMENT = 1000;
    public static final boolean[] EMPTY_WORD_STATISTIC = EncodeHelper.decodeOrCreate(null, COUNT_ELEMENT);
    public static final String NEXT_URL_TEMPLATE = "/top/next";
    public static final String PREV_URL_TEMPLATE = "/top/prev";
    public static final String RESET_URL_TEMPLATE = "/top/reset";
    public static final String UNKNOWWORDS_URL_TEMPLATE = "/top/unknowwords";
    public static final String ROOT_EXPRESSION = "$.*";
    public static final String WORD_EXPRESSION = "$.word";
    public static final String NUMBER_EXPRESSION = "$.number";
    public static final String FINISH_EXPRESSION = "$.finish";
    public static final String CURRENT_WORD = "currentWord";
    public static final String WORD_STATISTICS = "wordStatistics";
    public static final String IS_KNOW = "isKnow";
}
