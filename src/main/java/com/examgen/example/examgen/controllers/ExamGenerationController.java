package com.examgen.example.examgen.controllers;

import com.examgen.example.examgen.dto.ExamRequest;
import com.examgen.example.examgen.services.ExamRequestService;
import com.examgen.example.examgen.services.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "*")
public class ExamGenerationController {

    private final ExamRequestService examRequestService;
    private final TranslateService translateService;

    @Autowired
    public ExamGenerationController(ExamRequestService examRequestService, TranslateService translateService) {
        this.examRequestService = examRequestService;
        this.translateService = translateService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateExam(@RequestBody ExamRequest request) {
        try {
            if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Description is required");
            }
            
            // Detect original language to translate response back (with fallback)
            String originalLanguage;
            try {
                originalLanguage = translateService.detectLanguageOnly(request.getDescription());
            } catch (Exception e) {
                originalLanguage = "English"; // Default fallback
            }
            
            String exam = examRequestService.generateExam(request, originalLanguage);
            return ResponseEntity.ok(exam);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }
}