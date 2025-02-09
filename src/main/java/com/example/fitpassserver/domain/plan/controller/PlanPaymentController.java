package com.example.fitpassserver.domain.plan.controller;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PlanSubScriptionRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.KakaoPaymentService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import com.example.fitpassserver.domain.plan.dto.request.PlanChangeRequestDTO;
import com.example.fitpassserver.domain.plan.dto.response.FirstSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.KakaoCancelResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.PlanSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.SIDCheckResponseDTO;
import com.example.fitpassserver.domain.plan.service.PlanService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        coinPaymentHistoryService.createNewCoinPaymentByPlan(member, response.tid(), body);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/success")
    @Operation(summary = "플랜 1회차 정기 결제 성공 후 요청", description = "플랜 정기 결제 1회차에만 성공 요청시 실행되는 API")
    public ApiResponse<PlanSubscriptionResponseDTO> approveSinglePay(@CurrentMember Member member,
                                                                     @RequestParam("pg_token") String pgToken) {
        planService.checkOriginalPlan(member);
        CoinPaymentHistory history = coinPaymentHistoryService.getCurrentTidCoinPaymentHistory(member);
        PlanSubscriptionResponseDTO dto = paymentService.approveSubscription(pgToken, history.getTid());
        Coin coin = coinService.createSubscriptionNewCoin(member, history,
                planService.createNewPlan(member, dto.itemName(), dto.sid()));
        coinPaymentHistoryService.approve(history, coin);
        smsCertificationUtil.sendPlanPaymentSMS(member.getPhoneNumber(), dto.itemName(), dto.amount().total());
        return ApiResponse.onSuccess(dto);
    }

    @Operation(summary = "코인 정기 결제 실패", description = "결제 실패시 실행되는 API")
    @PostMapping("/fail")
    public ApiResponse<?> failSinglePay(@CurrentMember Member member) {
        coinPaymentHistoryService.fail(member);
        return ApiResponse.onSuccess("정기 결제가 실패되었습니다.");
    }

    @Operation(summary = "코인 정기 결제 취소", description = "결제 취소시 실행되는 API")
    @PostMapping("/cancel")
    public ApiResponse<?> cancelSinglePay(@CurrentMember Member member) {
        coinPaymentHistoryService.cancel(member);
        return ApiResponse.onSuccess("정기 결제가 취소되었습니다.");
    }

    @Operation(summary = "정기 결제 비활성화", description = "정기 결제를 비활성화 하기 위해 요청합니다.")
    @PostMapping("/deactivate")
    public ApiResponse<KakaoCancelResponseDTO> cancelSubscriptionPay(@CurrentMember Member member) {
        KakaoCancelResponseDTO response = paymentService.cancelSubscription(member);
        planService.cancelNewPlan(member);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "정기 결제 요금제 변경", description = "정기 결제 요금제를 변경하기 위해 요청합니다.")
    @PostMapping("/change")
    public ApiResponse<String> changeSubscriptionPay(@CurrentMember Member member,
                                                     @Parameter(description = "변경할 요금제 정보") @RequestBody @Valid PlanChangeRequestDTO dto) {
        String planName = planService.changeSubscription(member, dto);
        smsCertificationUtil.sendPlanChangeAlert(member.getPhoneNumber(),
                planName);
        return ApiResponse.onSuccess("요금제가 변경 되었습니다.");
    }

    @Operation(summary = "정기 결제 상태 확인", description = "정기 결제 활성화, 비활성화 체크를 위해 요청합니다.")
    @PostMapping("/sid-status")
    public ApiResponse<SIDCheckResponseDTO> checkSidStatus(@CurrentMember Member member) {
        SIDCheckResponseDTO response = paymentService.sidCheck(planService.getPlan(member));
        return ApiResponse.onSuccess(response);
    }


}
