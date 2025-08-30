package com.examgen.example.examgen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequest {
    private String description;
    private String difficulty = "medium";
    private Integer questionCount = 5;
    private String questionType = "mixed"; // mixed, multiple-choice, essay
}