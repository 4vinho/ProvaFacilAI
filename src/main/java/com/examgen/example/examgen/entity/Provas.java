package com.examgen.example.examgen.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.examgen.example.examgen.enums.nivelDificuldade;

@Data
@Entity
@Table(name = "prova")
@Getter
@Setter
public class Provas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioId;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "dificuldade")
    private nivelDificuldade dificuldade;
}
