package com.example.fitpassserver.domain.coinPaymentHistory.service;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.entity.CoinTypeEntity;
import com.example.fitpassserver.domain.coin.exception.CoinErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinException;
import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.coin.repository.CoinTypeRepository;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.event.CoinApprovedEvent;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.CoinSinglePayRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.CoinPaymentHistoryResponseListDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PortOneResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayErrorCode;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayException;
import com.example.fitpassserver.domain.coinPaymentHistory.repository.CoinPaymentRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.dto.response.PlanSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.SubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.entity.PlanTypeEntity;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import com.example.fitpassserver.domain.plan.repository.PlanTypeRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoinPaymentHistoryService {
    private final CoinPaymentRepository coinPaymentRepository;
    private final PlanTypeRepository planTypeRepository;
    private final CoinTypeRepository coinTypeRepository;
    private final CoinRepository coinRepository;
    private final PlanRepository planRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final String KAKAOPAY = "kakaopay";
    private final String PGPAY = "PG";

    public CoinPaymentHistory createNewCoinPayment(Member member, KakaoPaymentApproveDTO dto, Coin coin) {
        return createSinglePayCoin(member, dto.tid(), dto.amount().total(), coin, KAKAOPAY);
    }

    @Transactional
    public CoinPaymentHistory createReadyKakaoPayment(Member member, CoinSinglePayRequestDTO dto, String tid) {
        CoinTypeEntity coinType = coinTypeRepository.findByPrice(dto.totalAmount())
                .orElseThrow(() -> new CoinException(CoinErrorCode.COIN_NOT_FOUND));

        return coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod(KAKAOPAY)
                .isAgree(true)
                .paymentStatus(PaymentStatus.READY)
                .tid(tid)
                .member(member)
                .coinCount(coinType.getCoinQuantity())
                .paymentPrice(dto.totalAmount())
                .build());
    }

    //READY인 TID 조회
    public CoinPaymentHistory getReadyKakaoPayment(Member member, String tid){
        return coinPaymentRepository.findByMemberAndTidAndPaymentStatus(member, tid, PaymentStatus.READY)
                .orElseThrow(()-> new KakaoPayException(KakaoPayErrorCode.NO_TID_ERROR));
    }
    //결제 승인 확정 메서드
    @Transactional
    public void approveKakaoPayment(Member member, String tid, KakaoPaymentApproveDTO dto){
        CoinPaymentHistory history = getReadyKakaoPayment(member,tid);
        if(!history.getPaymentPrice().equals(dto.amount().total())) {
            throw new KakaoPayException(KakaoPayErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        history.changeStatus(PaymentStatus.PAY_SUCCESS);
        eventPublisher.publishEvent(new CoinApprovedEvent(history.getId()));
    }

    public CoinPaymentHistory getPaySuccessPayment(Long historyId) {
        return coinPaymentRepository.findByIdAndPaymentStatus(historyId, PaymentStatus.PAY_SUCCESS)
                .orElseThrow(() -> new KakaoPayException(KakaoPayErrorCode.NO_TID_ERROR));
    }


    public CoinPaymentHistory createNewCoinPaymentByScheduler(Member member, SubscriptionResponseDTO dto, Coin coin) {
        return createPlanCoin(member, dto.item_name(), dto.tid(), dto.amount().total(), coin, KAKAOPAY);
    }

    public CoinPaymentHistory createNewCoinPaymentByPlan(Member member, PlanSubscriptionResponseDTO dto,
                                                         Coin coin) {
        return createPlanCoin(member, dto.itemName(), dto.tid(), dto.amount().total(), coin, KAKAOPAY);
    }

    public CoinPaymentHistory createNewCoinPaymentByPGSinglePay(Member member, PortOneResponseDTO.SearchSinglePaymentDTO dto, Coin coin) {
        return createSinglePayCoin(member, dto.id(), dto.amount().paid(), coin, PGPAY);
    }

    public CoinPaymentHistoryResponseListDTO getCoinHistory(Member member, String query, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<CoinPaymentHistory> coinPaymentHistories;
        LocalDateTime createdAt = LocalDateTime.now();
        if (cursor != 0) {
            createdAt = coinPaymentRepository.findById(cursor).orElseThrow(() ->
                    new CoinException(CoinErrorCode.COIN_PAYMENT_NOT_FOUND)
            ).getCreatedAt();
        }

        if (query.toLowerCase().equals("all")) {
            coinPaymentHistories = coinPaymentRepository
                    .findAllByMemberAndCreatedAtLessThanAndCoinIsNotNullAndPaymentStatusOrderByCreatedAtDesc(
                            member, createdAt, PaymentStatus.SUCCESS, pageable);
        } else {
            coinPaymentHistories = coinPaymentRepository.findAllByQueryAndCreatedAtLessThanOrderByCreatedAtDesc(
                    query.toLowerCase(), createdAt, member, PaymentStatus.SUCCESS, pageable);
        }
        boolean isSubscribing = planRepository.existsByMemberAndPlanTypeNotAndPlanTypeIsNotNull(member, PlanType.NONE);
        List<CoinPaymentHistory> histories = coinPaymentHistories.getContent();
        return CoinPaymentHistoryResponseListDTO.builder()
                .items(histories.stream()
                        .map(CoinPaymentHistoryResponseListDTO.CoinPaymentHistoryResponseDTO::toCoinPaymentHistoryResponseDTO)
                        .toList())
                .isSubscribing(isSubscribing)
                .hasNext(coinPaymentHistories.hasNext())
                .cursor(coinPaymentHistories.hasNext() ?
                        histories.get(coinPaymentHistories.getNumberOfElements() - 1).getId()
                        : null)
                .size(coinPaymentHistories.getNumberOfElements())
                .build();
    }

    public CoinPaymentHistory getCurrentTidCoinPaymentHistory(Member member) {
        CoinPaymentHistory history = coinPaymentRepository.findFirst1ByMemberOrderByCreatedAtDesc(member)
                .orElseThrow(
                        () -> new KakaoPayException(KakaoPayErrorCode.MEMBER_NOT_FOUND)
                );
        if (history.isSuccess()) {
            throw new KakaoPayException(KakaoPayErrorCode.ALREADY_SUCCESS_ERROR);
        }
        return history;
    }

    @Transactional
    public void cancel(Member member, String tid) {
        CoinPaymentHistory history = getReadyKakaoPayment(member, tid);
        history.changeStatus(PaymentStatus.CANCEL);
        coinPaymentRepository.save(history);
    }

    @Transactional
    public void fail(Member member, String tid) {
        CoinPaymentHistory history = getReadyKakaoPayment(member, tid);
        history.changeStatus(PaymentStatus.FAIL);
        coinPaymentRepository.save(history);
    }


    public CoinPaymentHistory createPGSinglePayCoin(Member member, String paymentId, int price, Coin coin) {
        return this.createSinglePayCoin(member, paymentId, price, coin, PGPAY);
    }

    public CoinPaymentHistory createPGPlanPayCoin(Member member, String itemName, String paymentId, int price, Coin coin) {
        return this.createPlanCoin(member, itemName, paymentId, price, coin, PGPAY);
    }

    public CoinPaymentHistory createSinglePayCoin(Member member, String tid, int price, Coin coin, String paymentMethod) {
        CoinTypeEntity coinType = coinTypeRepository.findByPrice(price)
                .orElseThrow(() -> new CoinException(CoinErrorCode.COIN_NOT_FOUND));
        return coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod(paymentMethod)
                .isAgree(true)
                .paymentStatus(PaymentStatus.SUCCESS)
                .tid(tid)
                .coin(coin)
                .member(member)
                .coinCount((coinType.getCoinQuantity()))
                .paymentPrice(price)
                .build());
    }

    private CoinPaymentHistory createPlanCoin(Member member, String itemName, String tid, int price, Coin coin, String paymentMethod) {
        PlanType type = PlanType.getPlanType(itemName);
        if (type == null) {
            throw new PlanException(PlanErrorCode.PLAN_NAME_NOT_FOUND);
        }
        PlanTypeEntity planType = planTypeRepository.findByPlanType(type)
                .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NAME_NOT_FOUND));

        return coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod(paymentMethod)
                .isAgree(true)
                .paymentStatus(PaymentStatus.SUCCESS)
                .coin(coin)
                .tid(tid)
                .coinCount(planType.getCoinQuantity())
                .member(member)
                .paymentPrice(price)
                .build());
    }
}
