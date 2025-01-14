package com.example.fitpassserver.domain.fitness.Repository;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FitnessRepository extends JpaRepository<Fitness, Long> {
    List<Fitness> findByIsRecommendTrue();
    List<Fitness> findByNameContaining(String keyword);
    Page<Fitness> findByCategoryList_CategoryName(String categoryName, Pageable pageable);
    Page<Fitness> findByCategoryList_CategoryNameAndIdGreaterThan(
            String categoryName, Long cursor, Pageable pageable
    );
}
