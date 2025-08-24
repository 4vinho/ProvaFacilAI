package com.examgen.example.examgen.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class OllamaService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String url;
    private final String model;

    public OllamaService(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${ollama.api.base-url}") String url,
            String model
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.url = url;
        this.model = model;
    }

    public String generateResponse(String prompt) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("prompt", prompt);
        requestBody.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url + "/api/generate", HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonResponse = getJsonNode(response);
            return jsonResponse.get("response").asText();
        } else {
            throw new RuntimeException("Failed to generate response: " + response.getStatusCode());
        }
    }

    private JsonNode getJsonNode(ResponseEntity<String> response) {
        try {
            return objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
