package com.examgen.example.examgen.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public OllamaService(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            @Value("${ollama.api.base-url}") String url,
            @Value("${ollama.api.model}") String model
    ) {
        this.webClient = webClientBuilder.baseUrl(url).build();
        this.objectMapper = objectMapper;
        this.model = model;
    }

    public String request(String prompt) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("prompt", prompt);
        requestBody.put("stream", false);

        String responseBody = webClient.post()
                .uri("/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.isError(), clientResponse ->
                        Mono.error(new RuntimeException("Failed to generate response: " + clientResponse.statusCode())))
                .bodyToMono(String.class)
                .block();

        JsonNode jsonResponse = getJsonNode(responseBody);
        return jsonResponse.get("response").asText();
    }

    private JsonNode getJsonNode(String responseBody) {
        try {
            return objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
