package com.example.fitpassserver.owner.fitnessHistory.service;

import com.example.fitpassserver.owner.fitnessHistory.dto.RevenueHistoryResponseDTO;
import com.example.fitpassserver.owner.fitnessHistory.dto.UsageHistoryResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface FitnessHistoryService {
    UsageHistoryResponseDTO getFitnessUsageHistory(Long fitnessId, int page, int size);

    RevenueHistoryResponseDTO getFitnessRevenueHistory(Long fitnessId, int page, int size);
}
