package com.example.fitpassserver.owner.fitnessHistory.controller;

import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.owner.fitnessHistory.service.FitnessHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner/history")
@Tag(name = "업체 정산내역, 이용내역 API")
public class FitnessHistoryController {
    private final FitnessHistoryService fitnessHistoryService;

    @Operation(
            summary = "업체 정산내역",
            description = "월 별로 각 업체의 정산 내역을 조회합니다. 총 코인, 정산 금액을 표시합니다."
    )
    @GetMapping(value = "/month/{fitnessId}")
    public ApiResponse<?> getFitnessMonthlySettlement(@PathVariable Long fitnessId,
                                                      @Parameter(description = "페이지 번호 (기본값:0)", example = "0")
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @Parameter(description = "페이지 크기 (기본값:10)", example = "10")
                                                      @RequestParam(defaultValue = "0") int size) {
        return ApiResponse.onSuccess(fitnessHistoryService.getFitnessRevenueHistory(fitnessId, page, size));
    }

    @Operation(
            summary = "업체 이용내역",
            description = "각 업체의 이용 회원 내역을 조회합니다. 회원명(아이디), 패스 사용 일시, 정산 금액을 표시합니다."
    )
    @GetMapping(value = "/usage/{fitnessId}")
    public ApiResponse<?> getFitnessUsageDetails(@PathVariable Long fitnessId,
                                                 @Parameter(description = "페이지 번호 (기본값:0)", example = "0")
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @Parameter(description = "페이지 크기 (기본값:10)", example = "10")
                                                 @RequestParam(defaultValue = "0") int size) {

        return ApiResponse.onSuccess(fitnessHistoryService.getFitnessUsageHistory(fitnessId, page, size));
    }
}
