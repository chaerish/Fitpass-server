package com.example.fitpassserver.domain.review.repsotiory;

import com.example.fitpassserver.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByFitnessId(Long fitnessId, Pageable pageable);
}
