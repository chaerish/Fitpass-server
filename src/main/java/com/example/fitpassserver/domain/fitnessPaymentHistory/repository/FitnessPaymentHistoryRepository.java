package com.example.fitpassserver.domain.fitnessPaymentHistory.repository;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitnessPaymentHistory.entity.FitnessPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FitnessPaymentHistoryRepository extends JpaRepository<FitnessPaymentHistory, Long> {
    Optional<FitnessPaymentHistory> findByMemberFitness(MemberFitness memberFitness);
}
