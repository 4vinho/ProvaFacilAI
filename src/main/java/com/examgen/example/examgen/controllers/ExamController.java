package com.examgen.example.examgen.controllers;

import com.examgen.example.examgen.dto.ExamRequest;
import com.examgen.example.examgen.dto.ExamResponse;
import com.examgen.example.examgen.entity.Material;
import com.examgen.example.examgen.services.MaterialService;
import com.examgen.example.examgen.services.ExamGenerationService;
import com.examgen.example.examgen.services.TranslateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/provas")
@CrossOrigin(origins = "*")
public class ExamController {

    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);

    private final MaterialService materialService;
    private final ExamGenerationService examGenerationService;
    private final TranslateService translateService;

    @Autowired
    public ExamController(
            MaterialService materialService, 
            ExamGenerationService examGenerationService,
            TranslateService translateService
    ) {
        this.materialService = materialService;
        this.examGenerationService = examGenerationService;
        this.translateService = translateService;
    }

    @PostMapping("/gerar")
    public ResponseEntity<?> gerarProva(@RequestBody ExamRequest request) {
        try {
            logger.info("Solicitação de geração de prova: {}", request.getTitulo());

            // Validações básicas
            if (request.getPalavrasChave() == null || request.getPalavrasChave().isEmpty()) {
                return ResponseEntity.badRequest().body("Palavras-chave são obrigatórias");
            }
            if (request.getNumeroQuestoes() == null || request.getNumeroQuestoes() <= 0) {
                return ResponseEntity.badRequest().body("Número de questões deve ser maior que zero");
            }

            // 1. Buscar materiais relevantes usando as palavras-chave
            logger.info("Buscando materiais para palavras-chave: {}", request.getPalavrasChave());
            String queryBusca = String.join(" ", request.getPalavrasChave());
            List<Material> materiaisRelevantes = materialService.findSimilarMaterials(
                queryBusca, 
                Math.min(10, request.getNumeroQuestoes() * 2) // Busca mais materiais para ter variedade
            );

            if (materiaisRelevantes.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Nenhum material encontrado para as palavras-chave fornecidas");
            }

            // 2. Preparar contexto para geração da prova
            String contextoMateriais = materiaisRelevantes.stream()
                .map(material -> material.getTranslatedContent()) // Já está em inglês e resumido
                .collect(Collectors.joining("\n\n---\n\n"));

            // 3. Gerar a prova usando o contexto dos materiais
            String provaGerada = examGenerationService.generateExam(
                contextoMateriais,
                request.getDescricao(),
                request.getDificuldade().toString(),
                request.getNumeroQuestoes()
            );

            // 4. Traduzir a prova para o idioma solicitado (se não for inglês)
            String provaFinal = provaGerada;
            if (!request.getIdiomaResposta().toLowerCase().contains("english") && 
                !request.getIdiomaResposta().toLowerCase().contains("inglês")) {
                
                logger.info("Traduzindo prova para: {}", request.getIdiomaResposta());
                try {
                    provaFinal = translateService.translateFromEnglish(provaGerada, request.getIdiomaResposta());
                } catch (Exception e) {
                    logger.warn("Erro na tradução, mantendo em inglês: {}", e.getMessage());
                }
            }

            // 5. Preparar resposta
            ExamResponse response = new ExamResponse();
            response.setTitulo(request.getTitulo());
            response.setDescricao(request.getDescricao());
            response.setDificuldade(request.getDificuldade().toString());
            response.setStatus("GERADA");
            response.setIdioma(request.getIdiomaResposta());

            // Parse simples do conteúdo da prova em questões
            String[] linhasProva = provaFinal.split("\n");
            List<ExamResponse.QuestaoResponse> questoes = parseQuestoes(linhasProva);
            response.setQuestoes(questoes);

            logger.info("Prova gerada com sucesso: {} questões", questoes.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Erro ao gerar prova", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar prova: " + e.getMessage());
        }
    }

    @GetMapping("/minhas-provas")
    public ResponseEntity<?> listarMinhasProvas(@RequestParam(required = false) Integer usuarioId) {
        try {
            // TODO: Implementar listagem de provas salvas
            // Por enquanto retorna lista vazia
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            logger.error("Erro ao listar provas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar provas: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterProva(@PathVariable Integer id) {
        try {
            // TODO: Implementar busca de prova específica
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao obter prova", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter prova: " + e.getMessage());
        }
    }

    private List<ExamResponse.QuestaoResponse> parseQuestoes(String[] linhas) {
        List<ExamResponse.QuestaoResponse> questoes = new java.util.ArrayList<>();
        
        StringBuilder perguntaAtual = new StringBuilder();
        StringBuilder respostaAtual = new StringBuilder();
        boolean lendoResposta = false;
        
        for (String linha : linhas) {
            linha = linha.trim();
            if (linha.isEmpty()) continue;
            
            // Detecta início de nova questão (padrões como "1.", "Question 1:", etc.)
            if (linha.matches("^\\d+\\..*") || linha.matches("^Question \\d+:.*")) {
                // Salva questão anterior se existir
                if (perguntaAtual.length() > 0) {
                    questoes.add(new ExamResponse.QuestaoResponse(
                        perguntaAtual.toString().trim(),
                        respostaAtual.toString().trim(),
                        "mista"
                    ));
                }
                
                // Inicia nova questão
                perguntaAtual = new StringBuilder(linha);
                respostaAtual = new StringBuilder();
                lendoResposta = false;
                
            } else if (linha.toLowerCase().startsWith("answer:") || 
                      linha.toLowerCase().startsWith("resposta:")) {
                lendoResposta = true;
                respostaAtual.append(linha).append(" ");
                
            } else if (lendoResposta) {
                respostaAtual.append(linha).append(" ");
                
            } else {
                perguntaAtual.append(" ").append(linha);
            }
        }
        
        // Adiciona última questão
        if (perguntaAtual.length() > 0) {
            questoes.add(new ExamResponse.QuestaoResponse(
                perguntaAtual.toString().trim(),
                respostaAtual.toString().trim(),
                "mista"
            ));
        }
        
        return questoes;
    }
}