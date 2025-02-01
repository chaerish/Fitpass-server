package com.example.fitpassserver.domain.fitness.repository;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FitnessRepository extends JpaRepository<Fitness, Long> {
    List<Fitness> findByIsRecommendTrue();
    Page<Fitness> findByCategoryList_CategoryName(String categoryName, Pageable pageable);
    Page<Fitness> findByCategoryList_CategoryNameAndIdGreaterThan(
            String categoryName, Long cursor, Pageable pageable
    );
    @Query("SELECT f FROM Fitness f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY f.id ASC")
    List<Fitness> findTopByNameContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT f FROM Fitness f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND f.id > :cursor ORDER BY f.id ASC")
    List<Fitness> findNextByNameContaining(@Param("keyword") String keyword, @Param("cursor") Long cursor, Pageable pageable);

    Optional<Fitness> findFitnessById(Long Id);
}