package com.examgen.example.examgen.services;

import com.examgen.example.examgen.dto.EmbeddingRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

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
        // Fix base URL for embeddings endpoint
        String baseUrl = url.replace("/api/generate", "");
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.objectMapper = objectMapper;
        this.model = model;
    }


    public float[] generateEmbeddingVector(String text) {
        try {
            EmbeddingRequest requestBody = new EmbeddingRequest(model, text);

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

    public String generateEmbedding(String text) {
        try {
            float[] embedding = generateEmbeddingVector(text);
            return "Embedding generated successfully with " + embedding.length + " dimensions";
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate embedding", e);
        }
    }

    public String calculateSimilarity(String text1, String text2) {
        try {
            float[] embedding1 = generateEmbeddingVector(text1);
            float[] embedding2 = generateEmbeddingVector(text2);
            
            double similarity = cosineSimilarity(embedding1, embedding2);
            
            return String.format("Cosine similarity: %.4f", similarity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate similarity", e);
        }
    }

    private double cosineSimilarity(float[] vectorA, float[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
