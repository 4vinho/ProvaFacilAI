package com.examgen.example.examgen.dto;

public class TranslateRequest {
    private String text;
    private String targetLanguage = "English";

    public TranslateRequest() {}

    public TranslateRequest(String text, String targetLanguage) {
        this.text = text;
        this.targetLanguage = targetLanguage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }
}