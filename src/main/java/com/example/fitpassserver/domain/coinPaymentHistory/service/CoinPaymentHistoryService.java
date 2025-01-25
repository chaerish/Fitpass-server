package com.example.fitpassserver.domain.coinPaymentHistory.service;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.exception.CoinErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinException;
import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.CoinPaymentHistoryResponseListDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayErrorCode;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayException;
import com.example.fitpassserver.domain.coinPaymentHistory.repository.CoinPaymentRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CoinPaymentHistoryService {
    private final CoinPaymentRepository coinPaymentRepository;
    private final CoinRepository coinRepository;

    public void createNewCoinPayment(Member member, String tid, String methodName, Integer price) {
        coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod(methodName)
                .isAgree(true)
                .paymentStatus(PaymentStatus.READY)
                .tid(tid)
                .member(member)
                .paymentPrice(price)
                .build());
    }

    public CoinPaymentHistory createNewCoinHistory(Member member, String tid, Integer price) {
        return coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod("카카오페이 정기 결제") //todo: 수정
                .isAgree(true)
                .tid(tid)
                .paymentStatus(PaymentStatus.SUCCESS)
                .member(member)
                .paymentPrice(price)
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
            coinPaymentHistories = coinRepository.findAllByHistoryCreatedAtLessThanAndMemberIsOrderByCreatedAtDesc(createdAt, member, pageable);
        } else {
            coinPaymentHistories = coinRepository.findAllByQueryIsCreatedAtLessThanOrderByCreatedAtDesc(query, createdAt, member, pageable);
        }

        return CoinPaymentHistoryResponseListDTO.builder()
                .items(coinPaymentHistories.getContent().stream().map(CoinPaymentHistoryResponseListDTO.CoinPaymentHistoryResponseDTO::toCoinPaymentHistoryResponseDTO).toList())
                .hasNext(coinPaymentHistories.hasNext())
                .cursor(coinPaymentHistories.hasNext() ? coinPaymentHistories.getContent().get(coinPaymentHistories.getNumberOfElements() - 1).getHistory().getId() : null)
                .size(coinPaymentHistories.getNumberOfElements())
                .build();
    }


    public CoinPaymentHistory getCurrentTidCoinPaymentHistory(Member member) {
        return coinPaymentRepository.findFirst1ByMemberOrderByCreatedAtDesc(member)
                .orElseThrow(
                        () -> new KakaoPayException(KakaoPayErrorCode.MEMBER_NOT_FOUND)
                );
    }

    @Transactional
    public void approve(CoinPaymentHistory history) {
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
