package com.examgen.example.examgen.controllers;

import com.examgen.example.examgen.dto.MaterialResponse;
import com.examgen.example.examgen.dto.MaterialUploadRequest;
import com.examgen.example.examgen.services.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@CrossOrigin(origins = "*")
public class MaterialController {

    private final MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping("/upload")
    public ResponseEntity<MaterialResponse> uploadMaterial(@RequestBody MaterialUploadRequest request) {
        try {
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            
            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            
            MaterialResponse response = materialService.uploadMaterial(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponse>> getAllMaterials() {
        try {
            List<MaterialResponse> materials = materialService.getAllMaterials();
            return ResponseEntity.ok(materials);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/completed")
    public ResponseEntity<List<MaterialResponse>> getCompletedMaterials() {
        try {
            List<MaterialResponse> materials = materialService.getCompletedMaterials();
            return ResponseEntity.ok(materials);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}