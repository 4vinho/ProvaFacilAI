package com.examgen.example.examgen.controllers;

import com.examgen.example.examgen.dto.DetailedSummarizeRequest;
import com.examgen.example.examgen.dto.SummarizeRequest;
import com.examgen.example.examgen.services.SummarizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summarize")
@CrossOrigin(origins = "*")
public class SummarizationController {

    private final SummarizationService summarizationService;

    @Autowired
    public SummarizationController(SummarizationService summarizationService) {
        this.summarizationService = summarizationService;
    }

    @PostMapping
    public ResponseEntity<String> summarize(@RequestBody SummarizeRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Text is required");
            }
            String summary = summarizationService.summarize(request.getText());
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }

    @PostMapping("/detailed")
    public ResponseEntity<String> detailedSummarize(@RequestBody DetailedSummarizeRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Text is required");
            }
            
            String summary = summarizationService.detailedSummarize(request.getText(), request.getLength());
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }
}
