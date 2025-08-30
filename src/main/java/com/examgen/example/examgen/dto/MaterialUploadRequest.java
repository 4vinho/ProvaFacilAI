package com.examgen.example.examgen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialUploadRequest {
    private String titulo;
    private String conteudo;
    private String descricao;
    private List<String> palavrasChave;
    private Integer usuarioId;
}