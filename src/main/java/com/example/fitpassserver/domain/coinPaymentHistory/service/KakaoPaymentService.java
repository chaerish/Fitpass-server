package com.example.fitpassserver.domain.coinPaymentHistory.service;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.CoinSinglePayRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.KakaoPaymentRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PlanSubScriptionRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.SinglePayApproveRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PlanSubScriptionResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPaymentService {
    @Value("${kakaopay.secret-key}")
    private String secretKey;
    @Value("${kakaopay.cid}")
    private String cid;
    @Value("${kakaopay.monthly-fee-cid}")
    private String monthlyCid;
    @Value("kakaopay.order-id")
    private String orderId;
    @Value("kakaopay.user-id")
    private String userId;
    private final String BASE_URL = "https://open-api.kakaopay.com/online/v1/payment";
    private final String APPROVE_URL = "http://localhost:8080/coin/pay/success";
    private final String CANCEL_URL = "http://localhost:8080/coin/pay/cancel";
    private final String FAIL_URL = "http://localhost:8080/coin/pay/fail";

    @NotNull
    private WebClient getKakaoClient() {
        WebClient kakao = WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "SECRET_KEY " + secretKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        return kakao;
    }

    public KakaoPaymentResponseDTO ready(CoinSinglePayRequestDTO dto) {
        WebClient kakao = getKakaoClient();
        KakaoPaymentRequestDTO request = new KakaoPaymentRequestDTO(
                cid,
                orderId,
                userId,
                dto.itemName(),
                dto.quantity(),
                dto.totalAmount(),
                0,
                APPROVE_URL,
                CANCEL_URL,
                FAIL_URL
        );
        Mono<KakaoPaymentResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/ready")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(KakaoPaymentResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }

    public PlanSubScriptionResponseDTO ready(PlanSubScriptionRequestDTO dto) {
        WebClient kakao = getKakaoClient();
        KakaoPaymentRequestDTO request = new KakaoPaymentRequestDTO(
                monthlyCid,
                orderId,
                userId,
                dto.itemName(),
                1,
                dto.totalAmount(),
                0,
                APPROVE_URL,
                CANCEL_URL,
                FAIL_URL
        );
        Mono<PlanSubScriptionResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/ready")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PlanSubScriptionResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }

    public KakaoPaymentApproveDTO approve(String pgToken, String tid) {
        WebClient kakao = getKakaoClient();
        SinglePayApproveRequestDTO request = new SinglePayApproveRequestDTO(
                cid,
                tid,
                orderId,
                userId,
                pgToken
        );
        Mono<KakaoPaymentApproveDTO> response = kakao.post()
                .uri(BASE_URL + "/approve")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(KakaoPaymentApproveDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }

}
