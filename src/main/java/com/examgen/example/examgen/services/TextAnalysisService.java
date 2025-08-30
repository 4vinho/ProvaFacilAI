package com.examgen.example.examgen.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TextAnalysisService {

    private final OllamaService ollamaService;
    private final String chatModel;

    public TextAnalysisService(
            OllamaService ollamaService,
            @Value("${ollama.api.model-chat}") String chatModel
    ) {
        this.ollamaService = ollamaService;
        this.chatModel = chatModel;
    }

    public String analyzeText(String text) {
        String prompt = """
        Analyze the following text and provide:
        1. Main topics and themes
        2. Writing style and tone
        3. Target audience
        4. Key concepts mentioned
        5. Structure and organization
        
        Text to analyze:
        """ + text;
        
        return ollamaService.request(prompt, chatModel);
    }

    public String extractKeywords(String text) {
        String prompt = """
        Extract the most important keywords and key phrases from the following text.
        Return them as a ranked list, with the most important ones first.
        Include single words and meaningful phrases.
        
        Text:
        """ + text;
        
        return ollamaService.request(prompt, chatModel);
    }

    public String analyzeSentiment(String text) {
        String prompt = """
        Analyze the sentiment of the following text.
        Provide:
        1. Overall sentiment (positive, negative, neutral)
        2. Confidence level (high, medium, low)
        3. Emotional tone
        4. Brief explanation of your analysis
        
        Text:
        """ + text;
        
        return ollamaService.request(prompt, chatModel);
    }

    public String assessReadingDifficulty(String text) {
        String prompt = """
        Assess the reading difficulty of the following text.
        Consider:
        1. Vocabulary complexity
        2. Sentence structure
        3. Concept difficulty
        4. Target reading level (elementary, middle school, high school, college, graduate)
        5. Suggestions for simplification if needed
        
        Text:
        """ + text;
        
        return ollamaService.request(prompt, chatModel);
    }
}