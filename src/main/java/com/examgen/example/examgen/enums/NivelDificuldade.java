package com.examgen.example.examgen.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NivelDificuldade {

    MUITO_FACIL(1, "Muito Fácil"),
    FACIL(2, "Fácil"),
    MEDIO(3, "Médio"),
    DIFICIL(4, "Difícil"),
    MUITO_DIFICIL(5, "Muito Difícil");


    private final int id;
    private final String nome;
}
