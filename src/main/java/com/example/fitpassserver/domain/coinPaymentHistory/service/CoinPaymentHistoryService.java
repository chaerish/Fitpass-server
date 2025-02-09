package com.example.fitpassserver.domain.coinPaymentHistory.service;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.entity.CoinType;
import com.example.fitpassserver.domain.coin.exception.CoinErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinException;
import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.CoinSinglePayRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PlanSubScriptionRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.CoinPaymentHistoryResponseListDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayErrorCode;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayException;
import com.example.fitpassserver.domain.coinPaymentHistory.repository.CoinPaymentRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.dto.response.SubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import java.time.LocalDateTime;
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
    private final CoinRepository coinRepository;
    private final PlanRepository planRepository;

    public void createNewCoinPayment(Member member, String tid, CoinSinglePayRequestDTO dto) {
        int price = dto.totalAmount();
        int quantity = dto.quantity();
        CoinType type = CoinType.getCoinType(price, quantity);
        if (type == null) {
            throw new CoinException(CoinErrorCode.COIN_NOT_FOUND);
        }
        coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod(dto.methodName())
                .isAgree(true)
                .paymentStatus(PaymentStatus.READY)
                .tid(tid)
                .member(member)
                .coinCount(((long) type.getCount() * quantity))
                .paymentPrice(dto.totalAmount())
                .build());
    }

    public CoinPaymentHistory createNewCoinPaymentByScheduler(Member member, SubscriptionResponseDTO dto) {
        PlanType type = PlanType.getPlanType(dto.item_name());
        if (type == null) {
            throw new PlanException(PlanErrorCode.PLAN_NOT_FOUND);
        }
        return coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod("카카오페이 정기 결제") //todo: 수정
                .isAgree(true)
                .tid(dto.tid())
                .coinCount((long) type.getCoinQuantity())
                .paymentStatus(PaymentStatus.READY)
                .member(member)
                .paymentPrice(dto.amount().total())
                .build());
    }

    public void createNewCoinPaymentByPlan(Member member, String tid, PlanSubScriptionRequestDTO dto) {
        PlanType type = PlanType.getPlanType(dto.itemName());
        if (type == null) {
            throw new PlanException(PlanErrorCode.PLAN_NOT_FOUND);
        }
        coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod("카카오페이 정기 결제") //todo: 수정
                .isAgree(true)
                .tid(tid)
                .coinCount((long) type.getCoinQuantity())
                .paymentStatus(PaymentStatus.READY)
                .member(member)
                .paymentPrice(dto.totalAmount())
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

        return CoinPaymentHistoryResponseListDTO.builder()
                .items(coinPaymentHistories.getContent().stream()
                        .map(coin -> CoinPaymentHistoryResponseListDTO.CoinPaymentHistoryResponseDTO.toCoinPaymentHistoryResponseDTO(
                                coin,
                                coinPaymentRepository.findByCoinAndPaymentStatus(coin, PaymentStatus.SUCCESS)))
                        .toList())
                .isSubscribing(isSubscribing)
                .hasNext(coinPaymentHistories.hasNext())
                .cursor(coinPaymentHistories.hasNext() ? coinPaymentHistories.getContent()
                        .get(coinPaymentHistories.getNumberOfElements() - 1).getHistory().getId() : null)
                .size(coinPaymentHistories.getNumberOfElements())
                .build();
    }

//    public CoinPaymentHistory getCurrentTidCoinPaymentHistory(Member member) {
//        return coinPaymentRepository.findFirst1ByMemberOrderByCreatedAtDesc(member)
//                .orElseThrow(
//                        () -> new KakaoPayException(KakaoPayErrorCode.MEMBER_NOT_FOUND)
//                );
//    }

    public CoinPaymentHistory getCurrentTidCoinPaymentHistory(Member member) {
        return coinPaymentRepository.findFirst1ByMemberOrderByCreatedAtDesc(member)
                .orElseThrow(
                        () -> new KakaoPayException(KakaoPayErrorCode.MEMBER_NOT_FOUND)
                );
    }

    @Transactional
    public void approve(CoinPaymentHistory history, Coin coin) {
        history.setCoin(coin);
        history.changeStatus(PaymentStatus.SUCCESS);
        coinPaymentRepository.save(history);
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
