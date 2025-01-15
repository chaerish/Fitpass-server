package com.example.fitpassserver.domain.fitnessPaymentHistory.repository;

import com.example.fitpassserver.domain.fitnessPaymentHistory.entity.FitnessPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FitnessPaymentHistoryRepository extends JpaRepository<FitnessPaymentHistory, Long> {
}
