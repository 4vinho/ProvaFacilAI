package com.examgen.example.examgen.dto;

public class DetailedSummarizeRequest {
    private String text;
    private String length = "medium"; // short, medium, long

    public DetailedSummarizeRequest() {}

    public DetailedSummarizeRequest(String text, String length) {
        this.text = text;
        this.length = length;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}