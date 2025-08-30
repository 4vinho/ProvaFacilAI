package com.examgen.example.examgen.repository;

import com.examgen.example.examgen.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    
    List<Material> findByStatus(Material.MaterialStatus status);
    
    @Query(value = "SELECT * FROM materials m WHERE m.status = 'COMPLETED' ORDER BY m.id DESC LIMIT :limit", nativeQuery = true)
    List<Material> findCompletedMaterialsForSimilarity(@Param("limit") int limit);
    
    @Query("SELECT m FROM Material m WHERE m.status = :status")
    List<Material> findCompletedMaterials(@Param("status") Material.MaterialStatus status);
}