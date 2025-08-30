package com.examgen.example.examgen.controllers;

import com.examgen.example.examgen.dto.TextRequest;
import com.examgen.example.examgen.services.TextAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/text")
@CrossOrigin(origins = "*")
public class TextAnalysisController {

    private final TextAnalysisService textAnalysisService;

    @Autowired
    public TextAnalysisController(TextAnalysisService textAnalysisService) {
        this.textAnalysisService = textAnalysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeText(@RequestBody TextRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Text is required");
            }
            
            String analysis = textAnalysisService.analyzeText(request.getText());
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }

    @PostMapping("/keywords")
    public ResponseEntity<String> extractKeywords(@RequestBody TextRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Text is required");
            }
            
            String keywords = textAnalysisService.extractKeywords(request.getText());
            return ResponseEntity.ok(keywords);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }

    @PostMapping("/sentiment")
    public ResponseEntity<String> analyzeSentiment(@RequestBody TextRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Text is required");
            }
            
            String sentiment = textAnalysisService.analyzeSentiment(request.getText());
            return ResponseEntity.ok(sentiment);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }

    @PostMapping("/difficulty")
    public ResponseEntity<String> assessDifficulty(@RequestBody TextRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Text is required");
            }
            
            String difficulty = textAnalysisService.assessReadingDifficulty(request.getText());
            return ResponseEntity.ok(difficulty);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }
}