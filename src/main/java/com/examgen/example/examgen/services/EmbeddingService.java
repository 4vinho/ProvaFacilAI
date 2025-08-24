package com.examgen.example.examgen.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmbeddingService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public EmbeddingService(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            @Value("${ollama.api.base-url}") String url,
            @Value("${ollama.api.model-embed}") String model
    ) {
        this.webClient = webClientBuilder.baseUrl(url).build();
        this.objectMapper = objectMapper;
        this.model = model;
    }

    public float[] generateEmbedding(String text) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("prompt", text);

            String responseBody = webClient.post()
                    .uri("/api/embeddings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.isError(), clientResponse ->
                            Mono.error(new RuntimeException("Failed to generate embedding: " + clientResponse.statusCode())))
                    .bodyToMono(String.class)
                    .block();

            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            JsonNode embeddingNode = jsonResponse.get("embedding");

            float[] embedding = new float[embeddingNode.size()];
            for (int i = 0; i < embeddingNode.size(); i++) {
                embedding[i] = (float) embeddingNode.get(i).asDouble();
            }

            return embedding;

        } catch (Exception e) {
            System.err.println("Error generating embedding: " + e.getMessage());
            throw new RuntimeException("Failed to generate embedding", e);
        }
    }
}
