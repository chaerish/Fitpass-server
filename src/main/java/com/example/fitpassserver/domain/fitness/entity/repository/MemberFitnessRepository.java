package com.example.fitpassserver.domain.fitness.entity.repository;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFitnessRepository extends JpaRepository<MemberFitness, Long> {
}
