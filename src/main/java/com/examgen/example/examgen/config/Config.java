package com.examgen.example.examgen.config;

import com.examgen.example.examgen.services.EmbeddingService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.document.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class Config {

    @Bean
    public EmbeddingModel embeddingModel(EmbeddingService embeddingService) {
        return new EmbeddingModel() {
            @Override
            public EmbeddingResponse call(EmbeddingRequest request) {
                List<Embedding> embeddings = request.getInstructions().stream()
                    .map(text -> {
                        float[] embedding = embeddingService.generateEmbeddingVector(text);
                        return new Embedding(embedding, 0);
                    })
                    .collect(Collectors.toList());
                
                return new EmbeddingResponse(embeddings);
            }

            @Override
            public float[] embed(Document document) {
                return embeddingService.generateEmbeddingVector(document.getText());
            }
        };
    }
}
