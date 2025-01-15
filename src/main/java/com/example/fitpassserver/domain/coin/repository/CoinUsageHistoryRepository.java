package com.example.fitpassserver.domain.coin.repository;

import com.example.fitpassserver.domain.coin.entity.CoinUsageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinUsageHistoryRepository extends JpaRepository<CoinUsageHistory, Long> {
}
