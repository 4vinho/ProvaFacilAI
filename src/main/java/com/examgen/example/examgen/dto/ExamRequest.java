package com.examgen.example.examgen.dto;

public class ExamRequest {
    private String description;
    private String difficulty = "medium";
    private Integer questionCount = 5;
    private String questionType = "mixed"; // mixed, multiple-choice, essay

    public ExamRequest() {}

    public ExamRequest(String description, String difficulty, Integer questionCount, String questionType) {
        this.description = description;
        this.difficulty = difficulty;
        this.questionCount = questionCount;
        this.questionType = questionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}