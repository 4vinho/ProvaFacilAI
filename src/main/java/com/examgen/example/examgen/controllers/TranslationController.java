package com.examgen.example.examgen.controllers;

import com.examgen.example.examgen.dto.TextRequest;
import com.examgen.example.examgen.dto.TranslateRequest;
import com.examgen.example.examgen.services.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translate")
@CrossOrigin(origins = "*")
public class TranslationController {

    private final TranslateService translateService;

    @Autowired
    public TranslationController(TranslateService translateService) {
        this.translateService = translateService;
    }

    @PostMapping
    public ResponseEntity<String> translate(@RequestBody TranslateRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Text is required");
            }
            
            String translation = translateService.translate(request.getText(), request.getTargetLanguage());
            return ResponseEntity.ok(translation);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }

    @PostMapping("/detect")
    public ResponseEntity<String> detectLanguage(@RequestBody TextRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Text is required");
            }
            
            String detection = translateService.detectLanguage(request.getText());
            return ResponseEntity.ok(detection);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }
}
