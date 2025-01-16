package com.example.fitpassserver.domain.coinPaymentHistory.service;

import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayErrorCode;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayException;
import com.example.fitpassserver.domain.coinPaymentHistory.repository.CoinPaymentRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoinPaymentHistoryService {
    private final CoinPaymentRepository coinPaymentRepository;

    public void createNewCoinPayment(Member member, String tid, String methodName) {
        coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod(methodName)
                .isAgree(true)
                .tid(tid)
                .member(member)
                .build());
    }

    public CoinPaymentHistory createNewCoinHistory(Member member, String tid) {
        return coinPaymentRepository.save(CoinPaymentHistory.builder()
                .paymentMethod("카카오페이 정기 결제") //todo: 수정
                .isAgree(true)
                .tid(tid)
                .member(member)
                .build());
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
