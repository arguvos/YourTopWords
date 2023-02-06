package com.arguvos.yourtopwords.reversocontext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ReversoContextClient {
    private static final String BASE_URL = "https://context.reverso.net/";
    private static final String QUERY_SERVICE = "bst-query-service";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:77.0) Gecko/20100101 Firefox/77.0";
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    private static final String DICTIONARY_ENTRY_LIST = "dictionary_entry_list";
    private static final String TERM = "term";
    private static final String LIST = "list";
    private static final String SOURCE_TEXT = "s_text";
    private static final String TARGET_TEXT = "t_text";

    private final QueryServiceBody queryServiceBody;

    public ReversoContextClient(String sourceLang, String targetLang) {
        queryServiceBody = QueryServiceBody.builder()
                .sourceLang(sourceLang)
                .targetLang(targetLang)
                .build();
    }

    public List<String> getTranslations(String sourceText) throws IOException, ParseException {
        String json = requestTranslation(sourceText);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        return StreamSupport.stream(jsonNode.get(DICTIONARY_ENTRY_LIST).spliterator(), false)
                .map(e -> e.get(TERM).asText())
                .collect(Collectors.toList());
    }

    public List<Pair<String, String>> getTranslationSamples(String sourceText) throws IOException, ParseException {
        String json = requestTranslation(sourceText);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        return StreamSupport.stream(jsonNode.get(LIST).spliterator(), false)
                .map(e -> Pair.of(e.get(SOURCE_TEXT).asText(), e.get(TARGET_TEXT).asText()))
                .collect(Collectors.toList());
    }

    private String requestTranslation(String sourceText) throws IOException, ParseException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            queryServiceBody.setSourceText(sourceText);
            HttpPost post = new HttpPost(BASE_URL + QUERY_SERVICE);
            post.setHeader(HttpHeaders.USER_AGENT, DEFAULT_USER_AGENT);
            post.setHeader(HttpHeaders.CONTENT_TYPE, JSON_CONTENT_TYPE);
            post.setEntity(new StringEntity(objectMapper.writeValueAsString(queryServiceBody), ContentType.APPLICATION_JSON));
            HttpEntity entity = client.execute(post).getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        }
    }
}