package com.example.fitpassserver.domain.fitness.repository;

import com.example.fitpassserver.domain.fitness.entity.FitnessImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FitnessImageRepository extends JpaRepository<FitnessImage, Long> {
}
