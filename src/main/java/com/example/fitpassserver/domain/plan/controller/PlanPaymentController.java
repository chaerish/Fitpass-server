package com.example.fitpassserver.domain.plan.controller;

import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PlanSubScriptionRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.KakaoPaymentService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import com.example.fitpassserver.domain.plan.dto.response.FirstSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.KakaoCancelResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.PlanSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.SubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.service.PlanService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plan/pay")
@RequiredArgsConstructor
@Tag(name = "플랜 결제 API", description = "플랜 결제 API입니다.")
public class PlanPaymentController {
    private final KakaoPaymentService paymentService;
    private final CoinPaymentHistoryService coinPaymentHistoryService;
    private final CoinService coinService;
    private final PlanService planService;
    private final SmsCertificationUtil smsCertificationUtil;

    @Operation(summary = "코인 정기 결제 1회차 요청", description = "가장 처음 플랜을 등록시, 코인 정기 결제(플랜 구매)를 요청합니다.")
    @PostMapping("/first-request")
    public ApiResponse<FirstSubscriptionResponseDTO> requestFirstSubscriptionPay(@CurrentMember Member member,
                                                                                 @RequestBody @Valid PlanSubScriptionRequestDTO body) {
        FirstSubscriptionResponseDTO response = paymentService.ready(body);
        coinPaymentHistoryService.createNewCoinPayment(member, response.tid(), body.methodName());
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "코인 정기 결제 2회차 요청", description = "코인 정기 결제(플랜 구매)를 2회차일 때 요청합니다.")
    @PostMapping("/request")
    public ApiResponse<SubscriptionResponseDTO> requestSubscriptionPay(@CurrentMember Member member) {
        SubscriptionResponseDTO response = paymentService.ready(member);
        CoinPaymentHistory history = coinPaymentHistoryService.createNewCoinHistory(member, response.tid());
        coinService.createSubscriptionNewCoin(member, history, response.item_name());
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/success")
    @Operation(summary = "플랜 정기 결제 성공", description = "결제 성공시 실행되는 API")
    public ApiResponse<PlanSubscriptionResponseDTO> approveSinglePay(@CurrentMember Member member,
                                                                     @RequestParam("pg_token") String pgToken) {
        CoinPaymentHistory history = coinPaymentHistoryService.getCurrentTidCoinPaymentHistory(member);
        PlanSubscriptionResponseDTO dto = paymentService.approveSubscription(pgToken, history.getTid());
        coinService.createSubscriptionNewCoin(member, history, dto.itemName());
        planService.createNewPlan(member, dto.itemName(), dto.sid());
        coinPaymentHistoryService.approve(history);
        smsCertificationUtil.sendPlanPaymentSMS(member.getPhoneNumber(), dto.itemName(), dto.amount().total());
        return ApiResponse.onSuccess(dto);
    }

    @Operation(summary = "코인 정기 결제 실패", description = "결제 실패시 실행되는 API")
    @PostMapping("/fail")
    public ApiResponse<?> failSinglePay(@CurrentMember Member member) {
        coinPaymentHistoryService.fail(member);
        return ApiResponse.onSuccess("결제가 실패되었습니다.");
    }

    @Operation(summary = "코인 정기 결제 취소", description = "결제 취소시 실행되는 API")
    @PostMapping("/cancel")
    public ApiResponse<?> cancelSinglePay(@CurrentMember Member member) {
        coinPaymentHistoryService.cancel(member);
        return ApiResponse.onSuccess("결제가 취소되었습니다.");
    }

    @Operation(summary = "정기 결제 비활성화", description = "정기 결제를 비활성화 하기위해 요청합니다.")
    @PostMapping("/deactivate")
    public ApiResponse<KakaoCancelResponseDTO> cancelSubscriptionPay(@CurrentMember Member member) {
        KakaoCancelResponseDTO response = paymentService.subscriptionCancel(member);
        planService.cancelNewPlan(member);
        return ApiResponse.onSuccess(response);
    }


}
