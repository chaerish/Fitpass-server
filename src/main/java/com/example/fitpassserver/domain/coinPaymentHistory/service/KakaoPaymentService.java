package com.example.fitpassserver.domain.coinPaymentHistory.service;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.event.CoinSuccessEvent;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.CoinSinglePayRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.KakaoPaymentRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PlanSubScriptionRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.SinglePayApproveRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import com.example.fitpassserver.domain.plan.dto.event.PlanSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.RegularSubscriptionApprovedEvent;
import com.example.fitpassserver.domain.plan.dto.request.SIDCheckDTO;
import com.example.fitpassserver.domain.plan.dto.request.SubscriptionCancelRequestDTO;
import com.example.fitpassserver.domain.plan.dto.request.SubscriptionRequestDTO;
import com.example.fitpassserver.domain.plan.dto.response.FirstSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.KakaoCancelResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.PlanSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.SIDCheckResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.SubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanTypeEntity;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import com.example.fitpassserver.domain.plan.repository.PlanTypeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPaymentService {
    private final PlanTypeRepository planTypeRepository;

    @Value("${kakaopay.secret-key}")
    private String secretKey;
    @Value("${kakaopay.cid}")
    private String cid;
    @Value("${kakaopay.monthly-fee-cid}")
    private String monthlyCid;
    @Value("${kakaopay.monthly-fee-regular-cid}")
    private String monthlyRegularCid;
    @Value("${kakaopay.order-id}")
    private String orderId;
    @Value("${kakaopay.user-id}")
    private String userId;
    @Value("${kakaopay.approve-url}")
    private String APPROVE_URL;
    @Value("${kakaopay.cancel-url}")
    private String CANCEL_URL;
    @Value("${kakaopay.fail-url}")
    private String FAIL_URL;
    @Value("${kakaopay.plan-approve-url}")
    private String PLAN_APPROVE_URL;
    private final ApplicationEventPublisher eventPublisher;

    private final String BASE_URL = "https://open-api.kakaopay.com/online/v1/payment";
    private final PlanRepository planRepository;
    private final SmsCertificationUtil smsCertificationUtil;

    @NotNull
    private WebClient getKakaoClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "SECRET_KEY " + secretKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    /*
    결제 Ready
     */
    //단건 결제
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
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(KakaoPaymentResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }

    //정기 결제 첫번째
    public FirstSubscriptionResponseDTO ready(PlanSubScriptionRequestDTO dto) {
        WebClient kakao = getKakaoClient();
        KakaoPaymentRequestDTO request = new KakaoPaymentRequestDTO(
                monthlyCid,
                orderId,
                userId,
                dto.itemName(),
                1,
                dto.totalAmount(),
                0,
                PLAN_APPROVE_URL,
                CANCEL_URL,
                FAIL_URL
        );
        Mono<FirstSubscriptionResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/ready")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(FirstSubscriptionResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }

    //정기 결제 두번째 회차
    public void subscribe(Plan plan) {
        if (plan.getSid() == null) {
            throw new PlanException(PlanErrorCode.SID_NOT_FOUND);
        }
        PlanTypeEntity planTypeEntity = planTypeRepository.findByPlanType(plan.getPlanType())
                .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

        WebClient kakao = getKakaoClient();
        SubscriptionRequestDTO request = new SubscriptionRequestDTO(
                monthlyRegularCid,
                plan.getSid(),
                orderId,
                userId,
                plan.getPlanType().getName(),
                1,
                planTypeEntity.getPrice(),
                0
        );
        Mono<SubscriptionResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/subscription")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(SubscriptionResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        SubscriptionResponseDTO dto = response.block();
        eventPublisher.publishEvent(new RegularSubscriptionApprovedEvent(plan, dto));
    }

    /*
    결제 approve
     */
    //단건 결제 성공
    public KakaoPaymentApproveDTO approve(Member member, String pgToken, String tid) {
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
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(KakaoPaymentApproveDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        KakaoPaymentApproveDTO dto = response.block();
        eventPublisher.publishEvent(
                new CoinSuccessEvent(member, dto));
        return dto;
    }

    public PlanSubscriptionResponseDTO approveSubscription(Member member, String pgToken, String tid) {
        WebClient kakao = getKakaoClient();
        SinglePayApproveRequestDTO request = new SinglePayApproveRequestDTO(
                monthlyCid,
                tid,
                orderId,
                userId,
                pgToken
        );
        Mono<PlanSubscriptionResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/approve")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(PlanSubscriptionResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        PlanSubscriptionResponseDTO dto = response.block();
        eventPublisher.publishEvent(new PlanSuccessEvent(member, dto));
        return dto;
    }

    //정기 구독 취소
    public KakaoCancelResponseDTO cancelSubscription(Plan plan) {
        WebClient kakao = getKakaoClient();
        SubscriptionCancelRequestDTO request = new SubscriptionCancelRequestDTO(
                monthlyCid,
                plan.getSid()
        );
        Mono<KakaoCancelResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/manage/subscription/inactive")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(KakaoCancelResponseDTO.class)
                .doOnError((e) -> {

                    log.error("API Error {}", e.getMessage());
                });
        return response.block();
    }

    //sid 가 유효한지 체크
    public SIDCheckResponseDTO sidCheck(Plan plan) {
        WebClient kakao = getKakaoClient();
        SIDCheckDTO request = new SIDCheckDTO(
                monthlyCid,
                plan.getSid()
        );
        Mono<SIDCheckResponseDTO> response = kakao.post()
                .uri(BASE_URL + "/manage/subscription/status")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(SIDCheckResponseDTO.class)
                .doOnError((e) -> {
                    log.error("API Error {}", e.getMessage());
                });
        return response.block();

    }

    private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(errorBody -> {
                    log.error("API Error Response: {}", errorBody);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode root = objectMapper.readTree(errorBody);
                        int errorCode = root.path("error_code").asInt();
                        String methodResultCode = root.path("extras").path("method_result_code")
                                .asText();
                        if (errorCode == -782 && "8008".equals(methodResultCode)) {
                            return Mono.error(
                                    new PlanException(PlanErrorCode.PLAN_INSUFFICIENT_FUNDS));
                        } else {
                            return Mono.error(
                                    new PlanException(PlanErrorCode.KAKAO_PAY_ERROR));
                        }
                    } catch (Exception e) {
                        log.error("Parsing Error: {}", e.getMessage());
                    }
                    return Mono.error(
                            new RuntimeException("구독 실패 이유: " + errorBody));
                });
    }


}
