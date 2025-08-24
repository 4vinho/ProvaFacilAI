package com.examgen.example.examgen.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "questao")
@Getter
@Setter
public class Questao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "prova_id")
    private Usuario provaId;

    private String pergunta;

    private String resposta;
}
