package com.examgen.example.examgen.dto;

import com.examgen.example.examgen.enums.NivelDificuldade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequest {
    private String titulo;
    private String descricao;
    private List<String> palavrasChave;
    private NivelDificuldade dificuldade = NivelDificuldade.MEDIO;
    private Integer numeroQuestoes = 5;
    private String tipoQuestao = "mista"; // mista, multipla-escolha, dissertativa
    private String idiomaResposta = "Português"; // idioma de saída desejado
}