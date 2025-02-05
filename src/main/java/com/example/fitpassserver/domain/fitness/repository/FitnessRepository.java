package com.example.fitpassserver.domain.fitness.repository;

import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
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

    // 시설명으로 검색
    Page<Fitness> findByNameContaining(String name, Pageable pageable);

    // 카테고리로 검색
    @Query("SELECT f FROM Fitness f JOIN f.categoryList c WHERE c.categoryName LIKE %:category%")
    Page<Fitness> findByCategoryList_NameContaining(String category, Pageable pageable);

    // 전화번호로 검색
    Page<Fitness> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);
}