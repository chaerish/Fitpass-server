package com.example.fitpassserver.domain.coin.repository;

import com.example.fitpassserver.domain.coin.entity.CoinUsageHistory;
import com.example.fitpassserver.domain.fitnessPaymentHistory.entity.FitnessPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinUsageHistoryRepository extends JpaRepository<CoinUsageHistory, Long> {
    List<CoinUsageHistory> findAllByFitnessPaymentHistory(FitnessPaymentHistory fitnessPaymentHistory);
}
