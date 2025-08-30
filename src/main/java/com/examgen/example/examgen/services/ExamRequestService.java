package com.examgen.example.examgen.services;

import com.examgen.example.examgen.dto.ExamRequest;
import com.examgen.example.examgen.entity.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamRequestService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExamRequestService.class);
    
    private final MaterialService materialService;
    private final TranslateService translateService;
    private final OllamaService ollamaService;
    private final String chatModel;
    
    @Autowired
    public ExamRequestService(
            MaterialService materialService,
            TranslateService translateService,
            OllamaService ollamaService,
            @Value("${ollama.api.model-chat}") String chatModel
    ) {
        this.materialService = materialService;
        this.translateService = translateService;
        this.ollamaService = ollamaService;
        this.chatModel = chatModel;
    }
    
    public String generateExam(ExamRequest request, String originalLanguage) {
        try {
            logger.info("Starting exam generation for: {}", request.getDescription());
            
            // Step 1: Translate request to English (with fallback)
            String englishDescription;
            try {
                englishDescription = translateService.translateToEnglish(request.getDescription());
                logger.info("Translated description to English: {}", englishDescription);
            } catch (Exception e) {
                logger.warn("Translation failed, using original description");
                englishDescription = request.getDescription();
            }
            
            // Step 2: Find similar materials using similarity search
            List<Material> similarMaterials = materialService.findSimilarMaterials(englishDescription, 5);
            logger.info("Found {} similar materials", similarMaterials.size());
            
            // Step 3: Build context from similar materials
            StringBuilder contextBuilder = new StringBuilder();
            contextBuilder.append("REFERENCE MATERIALS:\n\n");
            
            for (int i = 0; i < similarMaterials.size(); i++) {
                Material material = similarMaterials.get(i);
                contextBuilder.append(String.format("Material %d - %s:\n", i + 1, material.getTitle()));
                contextBuilder.append(material.getTranslatedContent());
                contextBuilder.append("\n\n");
            }
            
            // Step 4: Generate exam in English using AI (with fallback)
            String englishExam;
            try {
                String examPrompt = buildExamPrompt(englishDescription, contextBuilder.toString(), request);
                englishExam = ollamaService.request(examPrompt, chatModel);
                logger.info("Generated exam in English");
            } catch (Exception e) {
                logger.warn("AI generation failed, using mock exam");
                englishExam = generateMockExam(englishDescription, request, similarMaterials);
            }
            
            // Step 5: Translate back to original language if needed (with fallback)
            if (originalLanguage.toLowerCase().contains("english")) {
                logger.info("Returning exam in English");
                return englishExam;
            } else {
                try {
                    logger.info("Translating exam back to: {}", originalLanguage);
                    return translateService.translateFromEnglish(englishExam, originalLanguage);
                } catch (Exception e) {
                    logger.warn("Translation back failed, returning in English");
                    return englishExam;
                }
            }
            
        } catch (Exception e) {
            logger.error("Error generating exam", e);
            throw new RuntimeException("Failed to generate exam: " + e.getMessage());
        }
    }
    
    private String buildExamPrompt(String description, String context, ExamRequest request) {
        String questionTypeInstruction = switch (request.getQuestionType().toLowerCase()) {
            case "multiple-choice" -> """
                Generate ONLY multiple choice questions.
                Format each question as:
                1. [Question text]
                A) [Option A]
                B) [Option B]
                C) [Option C]
                D) [Option D]
                Correct Answer: [Letter]
                """;
            case "essay" -> """
                Generate ONLY essay questions.
                Format each question as:
                1. [Question text]
                Expected length: [word count or time limit]
                Key points to address: [bullet points of main concepts]
                """;
            default -> """
                Generate a mix of question types including multiple choice, short answer, and essay questions.
                For multiple choice questions, use format:
                1. [Question] 
                A) B) C) D) with correct answer indicated
                
                For essay questions, include expected length and key points.
                """;
        };
        
        return String.format("""
            You are an expert exam generator. Create an exam based on the following requirements and reference materials.
            
            EXAM REQUIREMENTS:
            - Description: %s
            - Difficulty: %s
            - Number of Questions: %d
            - Question Type: %s
            
            %s
            
            %s
            
            INSTRUCTIONS:
            1. Base your questions on the provided reference materials
            2. Ensure questions test understanding of key concepts from the materials
            3. Make questions appropriate for the specified difficulty level
            4. Include a clear question structure and numbering
            5. Ensure the exam is comprehensive and well-organized
            6. Add a brief introduction explaining the exam content and structure
            
            Generate the exam now:
            """, 
            description, 
            request.getDifficulty(), 
            request.getQuestionCount(),
            request.getQuestionType(),
            questionTypeInstruction,
            context
        );
    }
    
    private String generateMockExam(String description, ExamRequest request, List<Material> materials) {
        StringBuilder exam = new StringBuilder();
        exam.append("MOCK EXAM - Generated without AI\n");
        exam.append("=================================\n\n");
        exam.append("Topic: ").append(description).append("\n");
        exam.append("Difficulty: ").append(request.getDifficulty()).append("\n");
        exam.append("Number of Questions: ").append(request.getQuestionCount()).append("\n\n");
        
        if (!materials.isEmpty()) {
            exam.append("Based on materials:\n");
            for (int i = 0; i < Math.min(materials.size(), 3); i++) {
                exam.append("- ").append(materials.get(i).getTitle()).append("\n");
            }
            exam.append("\n");
        }
        
        for (int i = 1; i <= request.getQuestionCount(); i++) {
            if ("multiple-choice".equals(request.getQuestionType())) {
                exam.append(String.format("%d. Sample multiple choice question %d about %s?\n", i, i, description));
                exam.append("A) Option A\n");
                exam.append("B) Option B\n");
                exam.append("C) Option C\n");
                exam.append("D) Option D\n");
                exam.append("Correct Answer: A\n\n");
            } else if ("essay".equals(request.getQuestionType())) {
                exam.append(String.format("%d. Sample essay question %d: Discuss the key concepts of %s.\n", i, i, description));
                exam.append("Expected length: 200-300 words\n");
                exam.append("Key points to address: Main concepts, examples, analysis\n\n");
            } else {
                if (i % 2 == 0) {
                    exam.append(String.format("%d. Short answer: What is %s?\n\n", i, description));
                } else {
                    exam.append(String.format("%d. Multiple choice question %d about %s?\n", i, i, description));
                    exam.append("A) Option A\n");
                    exam.append("B) Option B\n");
                    exam.append("C) Option C\n");
                    exam.append("D) Option D\n\n");
                }
            }
        }
        
        exam.append("Note: This is a mock exam generated due to AI service unavailability.\n");
        exam.append("Please ensure your AI models are properly configured for full functionality.");
        
        return exam.toString();
    }
}