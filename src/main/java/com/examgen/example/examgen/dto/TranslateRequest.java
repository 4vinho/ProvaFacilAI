package com.examgen.example.examgen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslateRequest {
    private String texto;
    private String idiomaOrigem; // auto-detecção se não informado
    private String idiomaDestino = "Inglês";
}