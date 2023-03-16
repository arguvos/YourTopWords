package com.arguvos.yourtopwords.api;


import com.arguvos.yourtopwords.service.TranslateCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("status")
public class StatusEndpoint {
    private final TranslateCache translateCache;

    @Autowired
    public StatusEndpoint(TranslateCache translateCache) {
        this.translateCache = translateCache;
    }

    @GetMapping()
    public ResponseEntity<Object> status() {
        if (!translateCache.isLoaded()) {
            return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
