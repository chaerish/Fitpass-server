package com.example.fitpassserver.domain.coinPaymentHistory.service;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.entity.CoinTypeEntity;
import com.example.fitpassserver.domain.coin.exception.CoinErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinException;
import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.coin.repository.CoinTypeRepository;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.CoinPaymentHistoryResponseListDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
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
    private final String KAKAOPAY = "kakaopay";

    public CoinPaymentHistory createNewCoinPayment(Member member, KakaoPaymentApproveDTO dto, Coin coin) {
        int price = dto.amount().total();
        CoinTypeEntity coinType = coinTypeRepository.findByPrice(price)
                .orElseThrow(() -> new CoinException(CoinErrorCode.COIN_NOT_FOUND));
        return coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod(KAKAOPAY)
                .isAgree(true)
                .paymentStatus(PaymentStatus.SUCCESS)
                .tid(dto.tid())
                .coin(coin)
                .member(member)
                .coinCount((coinType.getCoinQuantity() * dto.quantity()))
                .paymentPrice(dto.amount().total())
                .build());
    }

    public CoinPaymentHistory createNewCoinPaymentByScheduler(Member member, SubscriptionResponseDTO dto, Coin coin) {
        PlanType type = PlanType.getPlanType(dto.item_name());
        if (type == null) {
            throw new PlanException(PlanErrorCode.PLAN_NOT_FOUND);
        }
        PlanTypeEntity planType = planTypeRepository.findByPlanType(type)
                .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

        return coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod(KAKAOPAY)
                .paymentStatus(PaymentStatus.SUCCESS)
                .isAgree(true)
                .tid(dto.tid())
                .coinCount(planType.getCoinQuantity())
                .coin(coin)
                .member(member)
                .paymentPrice(dto.amount().total())
                .build());
    }

    public CoinPaymentHistory createNewCoinPaymentByPlan(Member member, PlanSubscriptionResponseDTO dto,
                                                         Coin coin) {
        PlanType type = PlanType.getPlanType(dto.itemName());
        if (type == null) {
            throw new PlanException(PlanErrorCode.PLAN_NAME_NOT_FOUND);
        }
        PlanTypeEntity planType = planTypeRepository.findByPlanType(type)
                .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NAME_NOT_FOUND));

        return coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod(KAKAOPAY)
                .isAgree(true)
                .paymentStatus(PaymentStatus.SUCCESS)
                .coin(coin)
                .tid(dto.tid())
                .coinCount(planType.getCoinQuantity())
                .member(member)
                .paymentPrice(dto.amount().total())
                .build());
    }

    public CoinPaymentHistoryResponseListDTO getCoinHistory(Member member, String query, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<Coin> coinPaymentHistories;
        LocalDateTime createdAt = LocalDateTime.now();
        if (cursor != 0) {
            createdAt = coinPaymentRepository.findById(cursor).orElseThrow(() ->
                    new CoinException(CoinErrorCode.COIN_PAYMENT_NOT_FOUND)
            ).getCreatedAt();
        }

        if (query.toLowerCase().equals("all")) {
            coinPaymentHistories = coinRepository.findAllByHistoryCreatedAtLessThanAndMemberIsOrderByCreatedAtDesc(
                    createdAt, member, pageable);
        } else {
            coinPaymentHistories = coinRepository.findAllByQueryIsCreatedAtLessThanOrderByCreatedAtDesc(query,
                    createdAt, member, pageable);
        }
        boolean isSubscribing = planRepository.existsByMemberAndPlanTypeNotAndPlanTypeIsNotNull(member, PlanType.NONE);
        List<Coin> coins = coinPaymentHistories.getContent();
        return CoinPaymentHistoryResponseListDTO.builder()
                .items(coins.stream()
                        .map(CoinPaymentHistoryResponseListDTO.CoinPaymentHistoryResponseDTO::toCoinPaymentHistoryResponseDTO)
                        .toList())
                .isSubscribing(isSubscribing)
                .hasNext(coinPaymentHistories.hasNext())
                .cursor(coinPaymentHistories.hasNext() ?
                        Optional.ofNullable(
                                        coinPaymentHistories.getContent().get(coinPaymentHistories.getNumberOfElements() - 1)
                                                .getHistory())
                                .map(CoinPaymentHistory::getId)
                                .orElse(null)
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
    public void cancel(Member member) {
        CoinPaymentHistory history = getCurrentTidCoinPaymentHistory(member);
        history.changeStatus(PaymentStatus.CANCEL);
        coinPaymentRepository.save(history);
    }

    @Transactional
    public void fail(Member member) {
        CoinPaymentHistory history = getCurrentTidCoinPaymentHistory(member);
        history.changeStatus(PaymentStatus.FAIL);
        coinPaymentRepository.save(history);
    }


}