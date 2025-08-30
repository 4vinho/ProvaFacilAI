package com.examgen.example.examgen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialSearchRequest {
    private List<String> palavrasChave;
    private Integer limite = 5;
    private Integer usuarioId;
}