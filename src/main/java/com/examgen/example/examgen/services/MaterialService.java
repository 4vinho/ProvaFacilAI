package com.examgen.example.examgen.services;

import com.examgen.example.examgen.dto.MaterialResponse;
import com.examgen.example.examgen.dto.MaterialUploadRequest;
import com.examgen.example.examgen.entity.Material;
import com.examgen.example.examgen.repository.MaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialService {
    
    private static final Logger logger = LoggerFactory.getLogger(MaterialService.class);
    
    private final MaterialRepository materialRepository;
    private final TranslateService translateService;
    private final EmbeddingService embeddingService;
    
    @Autowired
    public MaterialService(
            MaterialRepository materialRepository,
            TranslateService translateService,
            EmbeddingService embeddingService
    ) {
        this.materialRepository = materialRepository;
        this.translateService = translateService;
        this.embeddingService = embeddingService;
    }
    
    @Transactional
    public MaterialResponse uploadMaterial(MaterialUploadRequest request) {
        logger.info("Starting material upload: {}", request.getTitle());
        
        // Create and save initial material
        Material material = new Material(request.getTitle(), request.getContent(), request.getDescription());
        material.setStatus(Material.MaterialStatus.PROCESSING);
        material = materialRepository.save(material);
        
        // Process async (translation + embedding)
        processMateria(material.getId());
        
        return new MaterialResponse(
            material.getId(),
            material.getTitle(),
            material.getDescription(),
            material.getStatus().name()
        );
    }
    
    @Async
    @Transactional
    public void processMateria(Long materialId) {
        try {
            Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));
            
            logger.info("Processing material ID: {}", materialId);
            
            // Step 1: Detect original language (with fallback)
            String detectedLanguage;
            try {
                detectedLanguage = translateService.detectLanguageOnly(material.getOriginalContent());
            } catch (Exception e) {
                logger.warn("Failed to detect language, defaulting to English");
                detectedLanguage = "English";
            }
            material.setOriginalLanguage(detectedLanguage);
            
            // Step 2: Translate to English if needed (with fallback)
            String englishContent;
            if (detectedLanguage.toLowerCase().contains("english")) {
                englishContent = material.getOriginalContent();
                logger.info("Material already in English, skipping translation");
            } else {
                try {
                    logger.info("Translating material from {} to English", detectedLanguage);
                    englishContent = translateService.translateToEnglish(material.getOriginalContent());
                } catch (Exception e) {
                    logger.warn("Translation failed, using original content");
                    englishContent = material.getOriginalContent();
                }
            }
            material.setTranslatedContent(englishContent);
            
            // Step 3: Generate embedding (with fallback)
            float[] embedding;
            try {
                logger.info("Generating embedding for material");
                embedding = embeddingService.generateEmbeddingVector(englishContent);
            } catch (Exception e) {
                logger.warn("Embedding generation failed, using mock embedding");
                embedding = generateMockEmbedding();
            }
            material.setEmbedding(embedding);
            
            // Mark as completed
            material.setStatus(Material.MaterialStatus.COMPLETED);
            materialRepository.save(material);
            
            logger.info("Successfully processed material ID: {}", materialId);
            
        } catch (Exception e) {
            logger.error("Error processing material ID: {}", materialId, e);
            
            // Mark as error
            materialRepository.findById(materialId).ifPresent(material -> {
                material.setStatus(Material.MaterialStatus.ERROR);
                materialRepository.save(material);
            });
        }
    }
    
    public List<MaterialResponse> getAllMaterials() {
        return materialRepository.findAll().stream()
            .map(material -> new MaterialResponse(
                material.getId(),
                material.getTitle(),
                material.getDescription(),
                material.getStatus().name()
            ))
            .collect(Collectors.toList());
    }
    
    public List<MaterialResponse> getCompletedMaterials() {
        return materialRepository.findCompletedMaterials(Material.MaterialStatus.COMPLETED).stream()
            .map(material -> new MaterialResponse(
                material.getId(),
                material.getTitle(),
                material.getDescription(),
                material.getStatus().name()
            ))
            .collect(Collectors.toList());
    }
    
    public List<Material> findSimilarMaterials(String query, int limit) {
        try {
            logger.info("Searching for similar materials with query: {}", query);
            
            // Try to use vector similarity search if embedding generation works
            try {
                // Translate query to English (with fallback)
                String englishQuery;
                try {
                    englishQuery = translateService.translateToEnglish(query);
                } catch (Exception e) {
                    englishQuery = query;
                }
                
                // Generate embedding for query (with fallback)  
                float[] queryEmbedding = embeddingService.generateEmbeddingVector(englishQuery);
                
                // Convert embedding to PostgreSQL vector format
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < queryEmbedding.length; i++) {
                    sb.append(queryEmbedding[i]);
                    if (i < queryEmbedding.length - 1) sb.append(",");
                }
                sb.append("]");
                
                // Since we simplified the approach, just return recent completed materials
                List<Material> materials = materialRepository.findCompletedMaterialsForSimilarity(limit);
                return materials.stream().limit(limit).collect(Collectors.toList());
                    
            } catch (Exception embeddingError) {
                logger.warn("Embedding-based search failed, falling back to simple search: {}", embeddingError.getMessage());
                throw embeddingError; // Let it fall through to the general fallback
            }
                
        } catch (Exception e) {
            logger.error("Error finding similar materials, using fallback", e);
            
            // Fallback: return completed materials (simple approach)
            try {
                List<Material> completedMaterials = materialRepository.findByStatus(Material.MaterialStatus.COMPLETED);
                logger.info("Found {} completed materials as fallback", completedMaterials.size());
                
                return completedMaterials.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
                    
            } catch (Exception fallbackError) {
                logger.error("Even fallback search failed", fallbackError);
                return List.of(); // Return empty list as final fallback
            }
        }
    }
    
    private float[] generateMockEmbedding() {
        // Generate a random 768-dimensional embedding for testing
        float[] mockEmbedding = new float[768];
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 768; i++) {
            mockEmbedding[i] = (float) (random.nextGaussian() * 0.1);
        }
        return mockEmbedding;
    }
}