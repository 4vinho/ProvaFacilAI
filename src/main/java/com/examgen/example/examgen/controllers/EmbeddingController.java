package com.examgen.example.examgen.controllers;

import com.examgen.example.examgen.dto.SimilarityRequest;
import com.examgen.example.examgen.dto.TextRequest;
import com.examgen.example.examgen.services.EmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/embedding")
@CrossOrigin(origins = "*")
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    @Autowired
    public EmbeddingController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmbedding(@RequestBody TextRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Text is required");
            }
            
            String embedding = embeddingService.generateEmbedding(request.getText());
            return ResponseEntity.ok(embedding);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }

    @PostMapping("/similarity")
    public ResponseEntity<String> calculateSimilarity(@RequestBody SimilarityRequest request) {
        try {
            if (request.getText1() == null || request.getText1().trim().isEmpty() || 
                request.getText2() == null || request.getText2().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Both text1 and text2 are required");
            }
            
            String similarity = embeddingService.calculateSimilarity(request.getText1(), request.getText2());
            return ResponseEntity.ok(similarity);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }
}