package com.examgen.example.examgen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingRequest {

    private String model;
    private String prompt;
}