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
        Translate the following text.

        Text:
        """ + text;
        return ollamaService.request(prompt, translateModel);
    }
}
