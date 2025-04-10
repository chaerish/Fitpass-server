package com.example.fitpassserver.owner.dashboard.service;


import com.example.fitpassserver.owner.fitnessHistory.dto.RevenueHistoryResponseDTO;
import com.example.fitpassserver.owner.fitnessHistory.dto.UsageHistoryResponseDTO;
import com.example.fitpassserver.owner.owner.entity.Owner;

import java.util.Map;

public interface DashboardOwnerService {
    Map<String, Object> getNotices();

    RevenueHistoryResponseDTO getFitnessMonthRevenue(Owner owner, Long fitnessId);

    UsageHistoryResponseDTO getFitnessUsageHistory(Owner owner, Long fitnessId);
}
