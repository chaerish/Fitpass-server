package com.example.fitpassserver.domain.coinPaymentHistory.controller;

import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.CoinSinglePayRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.CompletePaymentRequest;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PGRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PaymentDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.StartPaymentRequest;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.CoinPaymentHistoryResponseListDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PGResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PaymentIdResponse;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.PortOneErrorCode;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.PortOneException;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryRedisService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.KakaoPaymentService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.NewCoinPaymentHistoryRedisService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.command.PGPaymentCommandService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.query.PGPaymentQueryService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.portone.sdk.server.webhook.Webhook;
import io.portone.sdk.server.webhook.WebhookTransaction;
import io.portone.sdk.server.webhook.WebhookVerifier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kotlin.Unit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
//@RequestMapping("/coin/pay")
@RequiredArgsConstructor
@Tag(name = "코인 결제 API", description = "코인 결제 API입니다.")
public class CoinPaymentController {
    private final KakaoPaymentService paymentService;
    private final CoinPaymentHistoryService coinPaymentHistoryService;
    private final CoinService coinService;
    private final CoinPaymentHistoryRedisService coinPaymentHistoryRedisService;
    private final PGPaymentCommandService pgPaymentCommandService;
    private final PGPaymentQueryService pgPaymentQueryService;

    private final WebhookVerifier portoneWebhook;
    private final NewCoinPaymentHistoryRedisService newCoinPaymentHistoryRedisService;

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
        KakaoPaymentApproveDTO dto = paymentService.approve(member, pgToken, tid);  //카카오 페이 결제 요청
        coinPaymentHistoryRedisService.deleteTid(memberId); //레디스에 저장되어있던 tid 삭제
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

    @Operation(summary = "PG사 단건 결제로 코인 추가", description = "PG사로 단건결제 이후에 코인 추가하기")
    @PostMapping("/pg/success")
    public ApiResponse<PGResponseDTO.PGSinglePayResponseDTO> pgSinglePay(@CurrentMember Member member,
                                                                         @RequestBody PGRequestDTO.PGSinglePayRequestDTO dto) {
        PGResponseDTO.PGSinglePayResponseDTO response = pgPaymentCommandService.pgSinglePay(member, dto);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "등록된 카드 목록 가져오기", description = "등록된 카드 정보 가져오기")
    @GetMapping("/pg/cards")
    public ApiResponse<PGResponseDTO.PGSearchCardListResponseDTO> findCards(@CurrentMember Member member) {
        PGResponseDTO.PGSearchCardListResponseDTO response = pgPaymentQueryService.findCards(member);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "등록된 카드로 결제하기", description = "등록된 카드로 결제하기")
    @PostMapping("/pg/billing-keys")
    public ApiResponse<PGResponseDTO.PGSinglePayResponseDTO> payWithBillingKey(@CurrentMember Member member,
                                                                               @RequestBody PGRequestDTO.PGPaymentWithBillingKeyRequestDTO dto) {

        PGResponseDTO.PGSinglePayResponseDTO response = pgPaymentCommandService.payWithBillingKey(member, dto);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/coin/pay/start")
    public ApiResponse<?> startPayment(
            @CurrentMember Member member,
            @RequestBody StartPaymentRequest request
    ) {
        PaymentIdResponse response = pgPaymentCommandService.createPaymentId(member, request);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "결제 정보를 실시간으로 전달받기 위한 웹훅입니다.")
    @PostMapping("/api/payment/webhook")
    public Mono<Unit> handleWebhook(
            @RequestBody String body,
            @RequestHeader("webhook-id") String webhookId,
            @RequestHeader("webhook-timestamp") String webhookTimestamp,
            @RequestHeader("webhook-signature") String webhookSignature
    ) throws PortOneException {
        Webhook webhook;
        try {
            webhook = portoneWebhook.verify(body, webhookId, webhookSignature, webhookTimestamp);
        } catch (Exception e) {
            throw new PortOneException(PortOneErrorCode.PORT_ONE_ERROR_CODE);
        }
        if (webhook instanceof WebhookTransaction transaction) {
            return pgPaymentCommandService.syncPayment(transaction.getData().getPaymentId(), null).map(payment -> Unit.INSTANCE);
        }
        return Mono.empty();
    }

    @PostMapping("/api/payment/complete")
    public Mono<PaymentDTO> completePayment(
            @CurrentMember Member member,
            @RequestBody CompletePaymentRequest completePaymentRequest
    ) {
        return pgPaymentCommandService.syncPayment(completePaymentRequest.paymentId(), member);
    }
}
