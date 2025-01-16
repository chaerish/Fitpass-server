package com.example.fitpassserver.domain.coinPaymentHistory.controller;

import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PlanSubScriptionRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PlanSubScriptionResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.KakaoPaymentService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/plan/pay")
@RequiredArgsConstructor
@Tag(name = "코인 결제 API", description = "코인 결제 API입니다.")
public class PlanPaymentController {
    private final KakaoPaymentService paymentService;
    private final CoinPaymentHistoryService coinPaymentHistoryService;
    private final CoinService coinService;

//    @Operation(summary = "코인 단건 결제 실패", description = "결제 실패시 실행되는 API")
//    @PostMapping("/fail")
//    public ApiResponse<?> failSinglePay(@CurrentMember Member member) {
//        coinPaymentHistoryService.fail(member);
//        return ApiResponse.onSuccess("결제가 실패되었습니다.");
//    }
//
//    @Operation(summary = "코인 단건 결제 취소", description = "결제 취소시 실행되는 API")
//    @PostMapping("/cancel")
//    public ApiResponse<?> cancelSinglePay(@CurrentMember Member member) {
//        coinPaymentHistoryService.cancel(member);
//        return ApiResponse.onSuccess("결제가 취소되었습니다.");
//    }

    @Operation(summary = "코인 정기 결제 요청", description = "코인 정기 결제(플랜 구매)를 요청합니다.")
    @PostMapping()
    public ApiResponse<PlanSubScriptionResponseDTO> requestSubscriptionPay(@CurrentMember Member member,
                                                                           @Valid PlanSubScriptionRequestDTO body) {
        PlanSubScriptionResponseDTO response = paymentService.ready(body);
        coinPaymentHistoryService.createNewCoinPayment(member, response.tid(), body.methodName());
        return ApiResponse.onSuccess(response);
    }

}
