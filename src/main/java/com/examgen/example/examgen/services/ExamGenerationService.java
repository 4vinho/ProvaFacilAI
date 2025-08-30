package com.examgen.example.examgen.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExamGenerationService {

    private final OllamaService ollamaService;
    private final String chatModel;

    public ExamGenerationService(
            OllamaService ollamaService,
            @Value("${ollama.api.model-chat}") String chatModel
    ) {
        this.ollamaService = ollamaService;
        this.chatModel = chatModel;
    }

    public String generateExam(String topic, String difficulty, Integer questionCount) {
        String prompt = String.format("""
        Generate an exam with %d questions about the topic: %s
        Difficulty level: %s
        
        Include a mix of multiple choice, short answer, and essay questions.
        Format the output clearly with question numbers and proper structure.
        For multiple choice questions, provide 4 options labeled A, B, C, D.
        
        Topic: %s
        """, questionCount, topic, difficulty, topic);
        
        return ollamaService.request(prompt, chatModel);
    }

    public String generateMultipleChoiceQuestions(String topic, String difficulty, Integer questionCount) {
        String prompt = String.format("""
        Generate %d multiple choice questions about the topic: %s
        Difficulty level: %s
        
        Format each question as:
        1. [Question text]
        A) [Option A]
        B) [Option B] 
        C) [Option C]
        D) [Option D]
        Correct Answer: [Letter]
        
        Make sure the questions test understanding of key concepts related to: %s
        """, questionCount, topic, difficulty, topic);
        
        return ollamaService.request(prompt, chatModel);
    }

    public String generateEssayQuestions(String topic, String difficulty, Integer questionCount) {
        String prompt = String.format("""
        Generate %d essay questions about the topic: %s
        Difficulty level: %s
        
        Format each question as:
        1. [Question text]
        Expected length: [word count or time limit]
        Key points to address: [bullet points of main concepts to cover]
        
        Make the questions thought-provoking and require critical analysis of: %s
        """, questionCount, topic, difficulty, topic);
        
        return ollamaService.request(prompt, chatModel);
    }
}