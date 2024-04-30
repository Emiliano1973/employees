package com.employees.demo.controllers;


import com.employees.demo.services.TitleService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services/titles")
@CacheConfig(cacheNames = "titles")
public class TitleController {

    private final TitleService titleService;

    public TitleController(TitleService titleService) {
        this.titleService = titleService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Cacheable(value = "titles", keyGenerator = "customKeyGenerator")
    public ResponseEntity<?> getAllTitles() {
        return ResponseEntity.ok(this.titleService.getAllTitles());
    }


}
