package com.examgen.example.examgen.dto;

public class ExamGenerationRequest {
    private String topic;
    private String difficulty = "medium";
    private Integer questionCount = 5;

    public ExamGenerationRequest() {}

    public ExamGenerationRequest(String topic, String difficulty, Integer questionCount) {
        this.topic = topic;
        this.difficulty = difficulty;
        this.questionCount = questionCount;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }
}