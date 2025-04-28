package com.example.fitpassserver.owner.dashboard.service;

import com.example.fitpassserver.admin.notice.service.NoticeAdminService;
import com.example.fitpassserver.owner.fitnessHistory.dto.RevenueHistoryResponseDTO;
import com.example.fitpassserver.owner.fitnessHistory.dto.UsageHistoryResponseDTO;
import com.example.fitpassserver.owner.fitnessHistory.service.FitnessHistoryService;
import com.example.fitpassserver.owner.owner.entity.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardOwnerServiceImpl implements DashboardOwnerService {

    private final NoticeAdminService noticeAdminService;
    private final FitnessHistoryService fitnessHistoryService;

    @Override
    public Map<String, Object> getNotices() {
        Pageable pageable = PageRequest.of(0, 3);
        return noticeAdminService.getNoticeAdminList("", pageable);
    }

    @Override
    public RevenueHistoryResponseDTO getFitnessMonthRevenue(Owner owner, Long fitnessId) {
        return fitnessHistoryService.getFitnessRevenueHistory(fitnessId, 0, 1);
    }

    @Override
    public UsageHistoryResponseDTO getFitnessUsageHistory(Owner owner, Long fitnessId) {
        return fitnessHistoryService.getFitnessUsageHistory(fitnessId, 0, 3);
    }
}
