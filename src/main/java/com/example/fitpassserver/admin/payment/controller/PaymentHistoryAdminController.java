package com.example.fitpassserver.admin.payment.controller;

import com.example.fitpassserver.admin.payment.service.PaymentAdminService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/payment/history")
@RestController
@RequiredArgsConstructor
@Tag(name = "구매내역 어드민 API")
public class PaymentHistoryAdminController {
    private final PaymentAdminService paymentAdminService;
    private final String PASS_TYPE = "패스";
    private final String COIN_TYPE = "코인";

    @Operation(summary = "admin 페이지 내에서 구매 내역을 조회하고, 회원명(memberName)을 이용해 검색합니다.")
    @GetMapping("")
    public ApiResponse<?> getPaymentHistory(
            @Parameter(description = "회원명으로 검색 시 회원 이름 입력") @RequestParam(required = false) String memberName,
            @Parameter(description = "코인/패스 중 타입, 정확히 코인, 패스라고 입력") @RequestParam String type,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page) {
        if (type.equals(COIN_TYPE)) {
            return ApiResponse.onSuccess(paymentAdminService.getCoinPaymentHistory(memberName, size, page));
        } else if (type.equals(PASS_TYPE)) {
            return ApiResponse.onSuccess(paymentAdminService.getPassPaymentHistory(memberName, size, page));
        }
        return ApiResponse.onFailure("400", "타입을 선택해주세요.");
    }
}
