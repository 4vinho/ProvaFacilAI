package com.examgen.example.examgen.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SummarizationService {

    private final OllamaService ollamaService;
    private final String summarizationModel;

    public SummarizationService(
            OllamaService ollamaService,
            @Value("${ollama.api.model-chat}") String summarizationModel
    ) {
        this.ollamaService = ollamaService;
        this.summarizationModel = summarizationModel;
    }

    public String summarize(String text) {
        String prompt = """
        Summarize the following text in a concise way, 
        keeping the most important keywords, entities, and concepts. 
        The output must be short and information-dense, 
        avoiding filler words.

        Text:
        """ + text;
        return ollamaService.request(prompt, summarizationModel);
    }

    public String detailedSummarize(String text, String length) {
        String lengthInstruction = switch (length.toLowerCase()) {
            case "short" -> "in 2-3 sentences";
            case "medium" -> "in 1-2 paragraphs";
            case "long" -> "in 3-4 paragraphs with detailed analysis";
            default -> "in 1-2 paragraphs";
        };

        String prompt = String.format("""
        Summarize the following text %s, 
        keeping the most important keywords, entities, and concepts. 
        Include key points, main arguments, and conclusions.
        Structure: Introduction sentence, main points, conclusion.

        Text:
        %s
        """, lengthInstruction, text);
        
        return ollamaService.request(prompt, summarizationModel);
    }
}
