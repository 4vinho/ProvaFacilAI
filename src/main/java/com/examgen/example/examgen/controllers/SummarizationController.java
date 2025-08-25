package com.examgen.example.examgen.controllers;

import com.examgen.example.examgen.services.SummarizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summarize")
public class SummarizationController {

    private final SummarizationService summarizationService;

    @Autowired
    public SummarizationController(SummarizationService summarizationService) {
        this.summarizationService = summarizationService;
    }

    @PostMapping
    public String summarize(@RequestBody String text) {
        return summarizationService.summarize(text);
    }
}
