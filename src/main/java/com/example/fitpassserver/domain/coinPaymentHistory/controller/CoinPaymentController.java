package com.example.fitpassserver.domain.coinPaymentHistory.controller;

import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.CoinSinglePayRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.CoinPaymentHistoryResponseListDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryRedisService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.KakaoPaymentService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coin/pay")
@RequiredArgsConstructor
@Tag(name = "코인 결제 API", description = "코인 결제 API입니다.")
public class CoinPaymentController {
    private final KakaoPaymentService paymentService;
    private final CoinPaymentHistoryService coinPaymentHistoryService;
    private final CoinService coinService;
    private final SmsCertificationUtil smsCertificationUtil;
    private final CoinPaymentHistoryRedisService coinPaymentHistoryRedisService;

    @Operation(summary = "코인 단건 결제 요청", description = "코인 단건 결제를 요청합니다.")
    @PostMapping("/request")
    public ApiResponse<KakaoPaymentResponseDTO> requestSinglePay(@CurrentMember Member member,
                                                                 @RequestBody @Valid CoinSinglePayRequestDTO body) {
        KakaoPaymentResponseDTO response = paymentService.ready(body);
        coinPaymentHistoryRedisService.saveTid(member.getId().toString(), response.tid());
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "코인 단건 결제 성공 요청", description = "결제 성공 요청시 실행되는 API")
    @PostMapping("/success")
    public ApiResponse<KakaoPaymentApproveDTO> approveSinglePay(@CurrentMember Member member,
                                                                @RequestParam("pg_token") String pgToken) {
        String memberId = member.getId().toString();
        String tid = coinPaymentHistoryRedisService.getTid(memberId);
        KakaoPaymentApproveDTO dto = paymentService.approve(pgToken, tid);
        CoinPaymentHistory history = coinPaymentHistoryService.createNewCoinPayment(member, tid, dto);
        coinPaymentHistoryService.approve(history, coinService.createNewCoin(member, history, dto));
        smsCertificationUtil.sendCoinPaymentSMS(member.getPhoneNumber(), dto.quantity(), dto.amount().total());
        coinPaymentHistoryRedisService.deleteTid(memberId);
        return ApiResponse.onSuccess(dto);
    }

    @Operation(summary = "코인 단건 결제 실패", description = "결제 실패시 실행되는 API")
    @PostMapping("/fail")
    public ApiResponse<?> failSinglePay(@CurrentMember Member member) {
        coinPaymentHistoryService.fail(member);
        return ApiResponse.onSuccess("결제가 실패되었습니다.");
    }

    @Operation(summary = "코인 단건 결제 취소", description = "결제 취소시 실행되는 API")
    @PostMapping("/cancel")
    public ApiResponse<?> cancelSinglePay(@CurrentMember Member member) {
        coinPaymentHistoryService.cancel(member);
        return ApiResponse.onSuccess("결제가 취소되었습니다.");
    }

    @Operation(summary = "코인 결제 내역 조회 API", description = "결제 내역 조회 API")
    @Parameters({
            @Parameter(name = "query", description = "전체: ALL(default), 요금제: PLAN, 코인: COIN")
    })
    @GetMapping("/history")
    public ApiResponse<CoinPaymentHistoryResponseListDTO> getCoinHistory(@CurrentMember Member member,
                                                                         @RequestParam(required = false, defaultValue = "ALL") String query,
                                                                         @RequestParam(required = false, defaultValue = "0") Long cursor,
                                                                         @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(coinPaymentHistoryService.getCoinHistory(member, query, cursor, size));
    }

}
