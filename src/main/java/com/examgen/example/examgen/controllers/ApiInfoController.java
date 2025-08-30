package com.examgen.example.examgen.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiInfoController {

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "ProvaFacil AI API");
        info.put("version", "1.0.0");
        info.put("description", "API para geração de provas, tradução, sumarização e análise de texto usando IA");
        info.put("timestamp", LocalDateTime.now());
        
        Map<String, String> endpoints = new HashMap<>();
        
        // Material Management
        endpoints.put("POST /api/materials/upload", "Upload de material educacional (traduz + embedding)");
        endpoints.put("GET /api/materials", "Listar todos os materiais");
        endpoints.put("GET /api/materials/completed", "Listar materiais processados");
        
        // Exam Generation (New Flow)
        endpoints.put("POST /api/exam/generate", "Gerar prova com busca por similaridade nos materiais");
        
        // Translation Services
        endpoints.put("POST /api/translate", "Traduzir texto");
        endpoints.put("POST /api/translate/detect", "Detectar idioma");
        
        // Text Analysis
        endpoints.put("POST /api/text/analyze", "Análise completa de texto");
        endpoints.put("POST /api/text/keywords", "Extração de palavras-chave");
        endpoints.put("POST /api/text/sentiment", "Análise de sentimento");
        endpoints.put("POST /api/text/difficulty", "Avaliação de dificuldade de leitura");
        
        // Embeddings
        endpoints.put("POST /api/embedding/generate", "Gerar embeddings");
        endpoints.put("POST /api/embedding/similarity", "Calcular similaridade");
        
        // Summarization
        endpoints.put("POST /api/summarize", "Sumarizar texto");
        endpoints.put("POST /api/summarize/detailed", "Sumarização detalhada");
        
        // System
        endpoints.put("GET /api/health", "Status de saúde da API");
        
        info.put("endpoints", endpoints);
        
        return ResponseEntity.ok(info);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "ProvaFacil AI API");
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getRoot() {
        Map<String, Object> welcome = new HashMap<>();
        welcome.put("message", "Bem-vindo à ProvaFacil AI API!");
        welcome.put("documentation", "Acesse /api/info para ver todos os endpoints disponíveis");
        welcome.put("health", "Acesse /api/health para verificar o status da API");
        welcome.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(welcome);
    }
}