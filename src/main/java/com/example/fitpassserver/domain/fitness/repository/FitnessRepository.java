package com.example.fitpassserver.domain.fitness.repository;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FitnessRepository extends JpaRepository<Fitness, Long> {
    List<Fitness> findByIsRecommendTrue();

    //카테고리로 검색
    Page<Fitness> findByCategoryList_CategoryNameAndIsPurchasableTrue(
            String categoryName, Pageable pageable);

    Page<Fitness> findByCategoryList_CategoryNameAndIdGreaterThanAndIsPurchasableTrue(
            String categoryName, Long cursor, Pageable pageable);

    //시설명으로 검색, 구매 가능한 것만 id 순으로
    @Query("SELECT f FROM Fitness f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND f.isPurchasable = true ORDER BY f.id ASC")
    List<Fitness> findTopByNameContainingAndIsPurchasableTrue(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT f FROM Fitness f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND f.id > :cursor AND f.isPurchasable = true ORDER BY f.id ASC")
    List<Fitness> findNextByNameContainingAndIsPurchasableTrue(@Param("keyword") String keyword, @Param("cursor") Long cursor, Pageable pageable);


    Optional<Fitness> findFitnessById(Long Id);

    // 시설명으로 검색
    Page<Fitness> findByNameContaining(String name, Pageable pageable);

    // 카테고리로 검색
    @Query("SELECT f FROM Fitness f JOIN f.categoryList c WHERE c.categoryName LIKE %:category%")
    Page<Fitness> findByCategoryList_NameContaining(String category, Pageable pageable);

    // 전화번호로 검색
    Page<Fitness> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);


    @Query("SELECT f FROM Fitness f WHERE f.owner.id = :ownerId ORDER BY f.id DESC")
    Slice<Fitness> findFirstPageByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("SELECT f FROM Fitness f WHERE f.owner.id = :ownerId AND f.id < :cursor ORDER BY f.id DESC")
    Slice<Fitness> findByOwnerAndCursor(@Param("ownerId") Long ownerId, @Param("cursor") Long cursor, Pageable pageable);
}