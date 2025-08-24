package com.examgen.example.examgen.services;

public class SummarizationService {

    private final String url;
    private final OllamaService ollamaService;

    public SummarizationService(String url, OllamaService ollamaService) {
        this.url = url;
        this.ollamaService = ollamaService;
    }

    public String summarize(String text) {

        String prompt = """
        Summarize the following text in a concise way, 
        keeping the most important keywords, entities, and concepts. 
        The output must be short and information-dense, 
        avoiding filler words.

        Text:
        """ + text;
        return ollamaService.request(prompt);
    }
}
