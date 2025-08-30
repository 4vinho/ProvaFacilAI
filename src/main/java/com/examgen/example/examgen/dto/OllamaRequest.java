package com.examgen.example.examgen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OllamaRequest {

    private String model;
    private String prompt;
    @JsonProperty("stream")
    private Boolean stream;

    public OllamaRequest() {
    }

    public OllamaRequest(String model, String prompt, Boolean stream) {
        this.model = model;
        this.prompt = prompt;
        this.stream = stream;
    }

    public OllamaRequest(String model, String prompt) {
        this.model = model;
        this.prompt = prompt;
        this.stream = false;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }
}
