package com.example.fitpassserver.owner.dashboard.service;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.owner.fitnessHistory.dto.RevenueHistoryResponseDTO;
import com.example.fitpassserver.owner.fitnessHistory.dto.UsageHistoryResponseDTO;
import java.util.Map;

public interface DashboardOwnerService {
    Map<String, Object> getNotices();

    RevenueHistoryResponseDTO getFitnessMonthRevenue(Member member, Long fitnessId);

    UsageHistoryResponseDTO getFitnessUsageHistory(Member member, Long fitnessId);
}
