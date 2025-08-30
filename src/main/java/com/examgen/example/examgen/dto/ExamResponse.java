package com.examgen.example.examgen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {
    private Integer id;
    private String titulo;
    private String descricao;
    private String dificuldade;
    private List<QuestaoResponse> questoes;
    private String status;
    private String idioma;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestaoResponse {
        private String pergunta;
        private String resposta;
        private String tipo;
    }
}