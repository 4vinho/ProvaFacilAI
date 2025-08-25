package com.examgen.example.examgen.controllers;

import com.examgen.example.examgen.services.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/translate")
public class TranslationController {

    private final TranslateService translateService;

    @Autowired
    public TranslationController(TranslateService translateService) {
        this.translateService = translateService;
    }

    @PostMapping
    public String translate(@RequestBody String text) {
        // Assuming TranslateService has a method like 'translate(String text)'
        // You might need to adjust this based on the actual TranslateService implementation
        return translateService.translate(text);
    }
}
