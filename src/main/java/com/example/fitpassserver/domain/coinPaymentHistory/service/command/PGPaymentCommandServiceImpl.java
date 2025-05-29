package com.example.fitpassserver.domain.coinPaymentHistory.service.command;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.entity.CoinTypeEntity;
import com.example.fitpassserver.domain.coin.exception.CoinErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinException;
import com.example.fitpassserver.domain.coin.repository.CoinTypeRepository;
import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PGRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PaymentDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.StartPaymentRequest;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PGResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PaymentIdResponse;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.PortOneErrorCode;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.PortOneException;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.NewCoinPaymentHistoryRedisService;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.repository.PlanTypeRepository;
import com.example.fitpassserver.domain.plan.service.PlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.portone.sdk.server.payment.PaidPayment;
import io.portone.sdk.server.payment.PaymentClient;
import io.portone.sdk.server.payment.VirtualAccountIssuedPayment;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PGPaymentCommandServiceImpl implements PGPaymentCommandService {

    private final CoinService coinService;
    private final CoinPaymentHistoryService coinPaymentHistoryService;
    private final PlanService planService;
    private final PlanTypeRepository planTypeRepository;
    private final CoinTypeRepository coinTypeRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, PaymentDTO> paymentStore = new HashMap<>();
    private final PaymentClient portone;
    private final NewCoinPaymentHistoryRedisService newCoinPaymentHistoryRedisService;

    @Override
    public PGResponseDTO.PGSinglePayResponseDTO payWithBillingKey(Member member, PGRequestDTO.PGPaymentWithBillingKeyRequestDTO dto) {
//        String paymentId = PortOnePaymentIdUtil.getRandomPaymentId();
//        Coin coin = coinService.createNewCoinByPg(member, paymentId, dto.amount());
//
//        CoinPaymentHistory coinPaymentHistory = coinPaymentHistoryService.createPGSinglePayCoin(member, paymentId, dto.amount(), coin);
//        coinService.setCoinAndCoinPayment(coin, coinPaymentHistory);
//
//        payWithBillingKey(member, paymentId, dto);
//        return PGResponseDTO.PGSinglePayResponseDTO.from(coin);
        return null;
    }

    @Override
    public PGResponseDTO.PGSinglePayResponseDTO pgSinglePay(Member member, PGRequestDTO.PGSinglePayRequestDTO dto) {
//        PortOneResponseDTO.SearchSinglePaymentDTO searchSinglePaymentDTO = portOneApiUtil.searchSinglePayment(dto.paymentId());
//        Coin coin = coinService.createNewCoinByPg(member, dto.paymentId(), searchSinglePaymentDTO.amount().paid());
//
//        CoinPaymentHistory coinPaymentHistory = coinPaymentHistoryService.createPGSinglePayCoin(member, dto.paymentId(), searchSinglePaymentDTO.amount().paid(), coin);
//        coinService.setCoinAndCoinPayment(coin, coinPaymentHistory);
//
//        return PGResponseDTO.PGSinglePayResponseDTO.from(coin);
        return null;
    }

    @Override
    public PGResponseDTO.PGSubscriptionPayResponseDTO createPGSubscriptionPay(Member member, PGRequestDTO.PGSubscriptionPaymentWithBillingKeyRequestDTO dto) {
//        String paymentId = PortOnePaymentIdUtil.getRandomPaymentId();
//        planService.checkOriginalPlan(member);
//
//        Plan plan = planService.createNewPGPlan(member, dto);
//        Coin coin = coinService.createSubscriptionNewCoin(member, plan);
//        CoinPaymentHistory coinPaymentHistory = coinPaymentHistoryService.createPGPlanPayCoin(member, dto.orderName(), paymentId, dto.amount(), coin);
//        PortOneRequestDTO.BillingKeyPaymentRequestDTO request = PortOneRequestDTO.BillingKeyPaymentRequestDTO.from(member.getId(), dto.billingKey(), dto.orderName(), dto.amount());
//        portOneApiUtil.payWithBillingKey(paymentId, request);
//        coinService.setCoinAndCoinPayment(coin, coinPaymentHistory);
//
//        return PGResponseDTO.PGSubscriptionPayResponseDTO.from(coin);
        return null;
    }

    @Override
    public void paySubscription(Plan plan) {
//        String paymentId = PortOnePaymentIdUtil.getRandomPaymentId();
//        Member member = plan.getMember();
//        PlanTypeEntity planType = planTypeRepository.findByPlanType(plan.getPlanType()).orElseThrow(() ->
//                new PlanException(PlanErrorCode.PLAN_NOT_FOUND));
//        PortOneRequestDTO.BillingKeyPaymentRequestDTO request = PortOneRequestDTO.BillingKeyPaymentRequestDTO.from(member.getId(), plan.getSid(), planType.getPlanType().getName(), planType.getCoinQuantity() + planType.getCoinAddition());
//        portOneApiUtil.payWithBillingKey(paymentId, request);
//        coinService.createSubscriptionNewCoin(member, plan);
    }

//    private PortOneResponseDTO.BillingKeyPaymentSummary payWithBillingKey(Member member, String paymentId, PGRequestDTO.PGPaymentWithBillingKeyRequestDTO dto) {
//        PortOneRequestDTO.BillingKeyPaymentRequestDTO request = PortOneRequestDTO.BillingKeyPaymentRequestDTO.from(member.getId(), dto.billingKey(), dto.orderName(), dto.amount());
//        PortOneResponseDTO.BillingKeyPaymentSummary response = portOneApiUtil.payWithBillingKey(paymentId, request);
//        return response;
//    }

    @Override
    public PaymentIdResponse createPaymentId(Member member, StartPaymentRequest request) {
        String paymentId = UUID.randomUUID().toString().replace("-", "");

        newCoinPaymentHistoryRedisService.savePaymentInfo(
                paymentId,
                member.getId(),
                request.itemId(),
                request.price()
        );

        return PaymentIdResponse.builder()
                .paymentId(paymentId)
                .build();
    }

    @Override
    public Mono<PaymentDTO> syncPayment(String paymentId, Member member) {
        PaymentDTO payment = paymentStore.get(paymentId);
        if (payment == null) {
            payment = new PaymentDTO("PENDING");
            paymentStore.put(paymentId, payment);
        }
        PaymentDTO finalPayment = payment;

        return Mono.fromFuture(portone.getPayment(paymentId))
                .onErrorMap(error -> {
                    log.error("PortOne 결제 조회 실패: {}", error.getMessage(), error);
                    return new PortOneException(PortOneErrorCode.PORT_ONE_ERROR_CODE);
                })
                .flatMap(actualPayment -> {
                    if (actualPayment instanceof PaidPayment paidPayment) {
                        if (!verifyPayment(paidPayment, member)) {
                            return Mono.error(new PortOneException(PortOneErrorCode.PORT_ONE_ERROR_CODE));
                        }
                        log.info("결제 성공 {}", actualPayment);
                        newCoinPaymentHistoryRedisService.deletePaymentInfo(paymentId);
                        if ("PAID".equals(finalPayment.status())) {
                            return Mono.just(finalPayment);
                        } else {
                            PaymentDTO newPayment = new PaymentDTO("PAID");
                            paymentStore.put(paymentId, newPayment);
                            return Mono.just(newPayment);
                        }
                    } else if (actualPayment instanceof VirtualAccountIssuedPayment) {
                        PaymentDTO newPayment = new PaymentDTO("VIRTUAL_ACCOUNT_ISSUED");
                        paymentStore.put(paymentId, newPayment);
                        return Mono.just(newPayment);
                    } else {
                        return Mono.just(finalPayment);
                    }
                });
    }

    private boolean verifyPayment(PaidPayment payment, Member member) {
        int paidAmount = (int) payment.getAmount().getTotal();

        CoinTypeEntity coinType = coinTypeRepository.findByPrice(paidAmount)
                .orElseThrow(() -> new CoinException(CoinErrorCode.COIN_NOT_FOUND));

        Coin coin = coinService.createNewCoinByPg(member, payment.getId(), paidAmount);

        CoinPaymentHistory coinPaymentHistory = coinPaymentHistoryService.createPGSinglePayCoin(member, payment.getId(), paidAmount, coin);
        coinService.setCoinAndCoinPayment(coin, coinPaymentHistory);

        return true;
    }
}
