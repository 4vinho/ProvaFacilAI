package com.examgen.example.examgen.services;

import com.examgen.example.examgen.dto.OllamaRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class OllamaService {
    private static final Logger logger = LoggerFactory.getLogger(OllamaService.class);
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OllamaService(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            @Value("${ollama.api.base-url}") String url
    ) {
        this.webClient = webClientBuilder.baseUrl(url).build();
        this.objectMapper = objectMapper;
        logger.info("OllamaService initialized with URL: {}", url);
    }

    public String request(String prompt, String modelName) {
        logger.info("=== OLLAMA REQUEST DEBUG ===");
        logger.info("Making request to Ollama with model: {} and prompt length: {}", modelName, prompt.length());
        logger.info("Prompt content: {}", prompt);

        OllamaRequest requestBody = new OllamaRequest(modelName, prompt, false);
        
        logger.info("Request body: model={}, prompt length={}, stream={}", 
                requestBody.getModel(), requestBody.getPrompt().length(), requestBody.getStream());
        
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            logger.info("JSON being sent to Ollama: {}", requestBodyJson);
        } catch (Exception e) {
            logger.error("Failed to serialize request body for logging", e);
        }

        try {
            String responseBody = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.isError(), clientResponse -> {
                        logger.error("HTTP error from Ollama: {}", clientResponse.statusCode());
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    logger.error("Error response body: {}", errorBody);
                                    return Mono.error(new RuntimeException("Ollama API error: " + clientResponse.statusCode() + " - " + errorBody));
                                });
                    })
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(120))
                    .block();

            logger.info("=== OLLAMA RESPONSE DEBUG ===");
            logger.info("Received response from Ollama with length: {}", responseBody != null ? responseBody.length() : 0);
            logger.info("Full response body: {}", responseBody);
            
            JsonNode jsonResponse = getJsonNode(responseBody);
            if (jsonResponse.has("error")) {
                String error = jsonResponse.get("error").asText();
                logger.error("Ollama returned error: {}", error);
                throw new RuntimeException("Ollama error: " + error);
            }
            
            if (!jsonResponse.has("response")) {
                logger.error("Ollama response missing 'response' field: {}", responseBody);
                throw new RuntimeException("Invalid response format from Ollama");
            }
            
            String response = jsonResponse.get("response").asText();
            logger.info("Extracted response text: {}", response);
            logger.info("=== END OLLAMA DEBUG ===");
            
            return response;
            
        } catch (WebClientResponseException e) {
            logger.error("WebClient error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to communicate with Ollama: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error communicating with Ollama", e);
            throw new RuntimeException("Failed to communicate with Ollama: " + e.getMessage(), e);
        }
    }

    private JsonNode getJsonNode(String responseBody) {
        try {
            return objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse Ollama response as JSON: {}", responseBody, e);
            throw new RuntimeException("Invalid JSON response from Ollama", e);
        }
    }
}
