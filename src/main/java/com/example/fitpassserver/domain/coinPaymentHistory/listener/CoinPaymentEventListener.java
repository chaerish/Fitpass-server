package com.example.fitpassserver.domain.coinPaymentHistory.listener;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.event.CoinApprovedEvent;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.event.CoinPaymentAllSuccessEvent;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinIssueService;
import com.example.fitpassserver.domain.kakaoNotice.util.KakaoAlimtalkUtil;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoinPaymentEventListener {
    private final KakaoAlimtalkUtil kakaoAlimtalkUtil;
    private final SmsCertificationUtil smsCertificationUtil;
    private final CoinIssueService coinIssueService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CoinApprovedEvent event) {
        try {
            coinIssueService.issueCoin(event.historyId());
        } catch (Exception e) {
            coinIssueService.markCoinIssueFailed(event.historyId());
            throw e;
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handle(CoinPaymentAllSuccessEvent event) {
        kakaoAlimtalkUtil.sendCoinOrPlanPayment(event.phoneNumber(), event.quantity() + "코인", event.paymentMethod());
        log.info("{} 에게 문자 발송 완료", event.phoneNumber());
    }

}
