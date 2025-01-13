package com.example.fitpassserver.domain.fitness.entity.repository;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FitnessRepository extends JpaRepository<Fitness, Long> {
}