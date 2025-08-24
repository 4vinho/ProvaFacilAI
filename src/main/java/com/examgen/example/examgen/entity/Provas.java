package com.examgen.example.examgen.entity;

import jakarta.persistence.*;
import com.examgen.example.examgen.enums.NivelDificuldade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "prova")
@NoArgsConstructor
@AllArgsConstructor
public class Provas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioId;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "dificuldade")
    private NivelDificuldade dificuldade;
}
