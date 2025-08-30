package com.examgen.example.examgen.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TranslateService {

    private final OllamaService ollamaService;
    private final String translateModel;

    public TranslateService(
            OllamaService ollamaService,
            @Value("${ollama.api.model-translate}") String translateModel
    ) {
        this.ollamaService = ollamaService;
        this.translateModel = translateModel;
    }
    public String translate(String text) {
        String prompt = """
        Translate the following text to English.

        Text:
        """ + text;
        return ollamaService.request(prompt, translateModel);
    }

    public String translate(String text, String targetLanguage) {
        String prompt = String.format("""
        Translate the following text to %s.
        Maintain the original meaning and context.
        If the text is already in %s, return it as is.

        Text:
        %s
        """, targetLanguage, targetLanguage, text);
        
        return ollamaService.request(prompt, translateModel);
    }

    public String detectLanguage(String text) {
        String prompt = """
        Detect the language of the following text.
        Provide the language name and confidence level.
        Format: Language: [language name], Confidence: [high/medium/low]

        Text:
        """ + text;
        
        return ollamaService.request(prompt, translateModel);
    }
    
    public String detectLanguageOnly(String text) {
        String prompt = """
        Detect the language of the following text.
        Respond only with the language name (e.g., "English", "Portuguese", "Spanish", etc.).

        Text:
        """ + text;
        
        return ollamaService.request(prompt, translateModel);
    }
    
    public String translateToEnglish(String text) {
        String prompt = """
        Translate the following text to English.
        Maintain the original meaning and context.
        If the text is already in English, return it as is.

        Text:
        """ + text;
        
        return ollamaService.request(prompt, translateModel);
    }
    
    public String translateFromEnglish(String text, String targetLanguage) {
        String prompt = String.format("""
        Translate the following text from English to %s.
        Maintain the original meaning and context.

        Text:
        %s
        """, targetLanguage, text);
        
        return ollamaService.request(prompt, translateModel);
    }
}
