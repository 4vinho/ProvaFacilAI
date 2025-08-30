package com.examgen.example.examgen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamGenerationRequest {
    private String topic;
    private String difficulty = "medium";
    private Integer questionCount = 5;
}