package com.example.fitpassserver.owner.dashboard.controller;

import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.owner.dashboard.service.DashboardOwnerService;
import com.example.fitpassserver.owner.fitnessHistory.dto.RevenueHistoryResponseDTO;
import com.example.fitpassserver.owner.fitnessHistory.dto.UsageHistoryResponseDTO;
import com.example.fitpassserver.owner.owner.annotation.CurrentOwner;
import com.example.fitpassserver.owner.owner.entity.Owner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/owner/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard 사장님 API")
public class DashboardOwnerController {

    private final DashboardOwnerService dashboardOwnerService;

    @Operation(summary = "공지사항 3개 조회 API", description = "공지사항 3개 조회합니다.")
    @GetMapping("/notices/preview")
    public ApiResponse<?> getNoticesPreview(@CurrentOwner Owner owner) {
        Map<String, Object> notices = dashboardOwnerService.getNotices();
        return ApiResponse.onSuccess(notices);
    }

    @Operation(summary = "이번 달 정산금액 조회 API", description = "이번 달 정산 금액을 조회합니다.")
    @GetMapping("/settlements/month/{fitnessId}")
    public ApiResponse<?> getMonthSettlement(
            @CurrentOwner Owner owner,
            @PathVariable Long fitnessId
    ) {
        RevenueHistoryResponseDTO res = dashboardOwnerService.getFitnessMonthRevenue(owner, fitnessId);
        return ApiResponse.onSuccess(res);
    }

    @Operation(summary = "회원 이용내역 3개 조회 API", description = "각 업체의 이용 회원 내역을 3개 조회합니다.")
    @GetMapping("/usages/preview/{fitnessId}")
    public ApiResponse<?> getFitnessUsageHistoryPreview(
            @CurrentOwner Owner owner,
            @PathVariable Long fitnessId
    ) {
        UsageHistoryResponseDTO res = dashboardOwnerService.getFitnessUsageHistory(owner, fitnessId);
        return ApiResponse.onSuccess(res);
    }
}
