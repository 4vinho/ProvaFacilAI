package com.examgen.example.examgen.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "materials")
public class Material {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String originalContent;
    
    @Column(columnDefinition = "TEXT")
    private String translatedContent; // Content translated to English
    
    @Column
    private String description;
    
    @JsonIgnore
    @Column(name = "embedding", columnDefinition = "TEXT")
    private String embedding;
    
    @Column
    private String originalLanguage;
    
    @Enumerated(EnumType.STRING)
    private MaterialStatus status = MaterialStatus.PROCESSING;
    
    public enum MaterialStatus {
        PROCESSING, COMPLETED, ERROR
    }
    
    // Constructors
    public Material() {}
    
    public Material(String title, String originalContent, String description) {
        this.title = title;
        this.originalContent = originalContent;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getOriginalContent() {
        return originalContent;
    }
    
    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }
    
    public String getTranslatedContent() {
        return translatedContent;
    }
    
    public void setTranslatedContent(String translatedContent) {
        this.translatedContent = translatedContent;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getEmbedding() {
        return embedding;
    }
    
    public void setEmbedding(String embedding) {
        this.embedding = embedding;
    }
    
    public void setEmbedding(float[] embeddingArray) {
        if (embeddingArray == null) {
            this.embedding = null;
            return;
        }
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embeddingArray.length; i++) {
            sb.append(embeddingArray[i]);
            if (i < embeddingArray.length - 1) sb.append(",");
        }
        sb.append("]");
        this.embedding = sb.toString();
    }
    
    public float[] getEmbeddingAsArray() {
        if (embedding == null || embedding.trim().isEmpty()) {
            return new float[0];
        }
        
        try {
            String[] parts = embedding.replace("[", "").replace("]", "").split(",");
            float[] result = new float[parts.length];
            for (int i = 0; i < parts.length; i++) {
                result[i] = Float.parseFloat(parts[i].trim());
            }
            return result;
        } catch (Exception e) {
            return new float[0];
        }
    }
    
    public String getOriginalLanguage() {
        return originalLanguage;
    }
    
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }
    
    public MaterialStatus getStatus() {
        return status;
    }
    
    public void setStatus(MaterialStatus status) {
        this.status = status;
    }
}