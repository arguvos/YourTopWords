package com.arguvos.yourtopwords.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("language")
public class LanguageEndpoint {
    @Value("${languages}")
    private String[] languages;

    @GetMapping("/available")
    public ResponseEntity<Object> getAvailableLanguage() {
        return new ResponseEntity<>(languages, HttpStatus.OK);
    }
}
