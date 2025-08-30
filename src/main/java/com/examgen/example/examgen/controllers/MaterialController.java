package com.examgen.example.examgen.controllers;

import com.examgen.example.examgen.dto.MaterialResponse;
import com.examgen.example.examgen.dto.MaterialUploadRequest;
import com.examgen.example.examgen.services.MaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materiais")
@CrossOrigin(origins = "*")
public class MaterialController {

    private static final Logger logger = LoggerFactory.getLogger(MaterialController.class);
    private final MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMaterial(@RequestBody MaterialUploadRequest request) {
        try {
            logger.info("Recebendo upload de material: {}", request.getTitulo());

            if (request.getTitulo() == null || request.getTitulo().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Título é obrigatório");
            }
            if (request.getConteudo() == null || request.getConteudo().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Conteúdo é obrigatório");
            }

            // O MaterialService já faz todo o processamento interno:
            // 1. Detecta idioma
            // 2. Traduz para inglês se necessário 
            // 3. Resumo do conteúdo
            // 4. Gera embedding
            // 5. Salva no banco
            MaterialResponse response = materialService.uploadMaterial(request);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Erro ao fazer upload do material", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar material: " + e.getMessage());
        }
    }

    @GetMapping("/meus-materiais")
    public ResponseEntity<?> listarMeusMateriais(@RequestParam(required = false) Integer usuarioId) {
        try {
            // Por enquanto lista todos - depois implementar filtro por usuário
            List<MaterialResponse> materiais = materialService.getAllMaterials();
            return ResponseEntity.ok(materiais);
        } catch (Exception e) {
            logger.error("Erro ao listar materiais", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar materiais: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMaterial(@PathVariable Long id) {
        try {
            // TODO: Implementar deleção no MaterialService
            return ResponseEntity.ok("Material deletado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar material", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar material: " + e.getMessage());
        }
    }
}