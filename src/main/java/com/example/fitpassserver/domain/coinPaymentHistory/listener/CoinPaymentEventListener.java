package com.example.fitpassserver.domain.coinPaymentHistory.listener;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.event.CoinPaymentAllSuccessEvent;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.event.CoinSuccessEvent;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.kakaoNotice.util.KakaoAlimtalkUtil;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoinPaymentEventListener {
    private final KakaoAlimtalkUtil kakaoAlimtalkUtil;
    private final SmsCertificationUtil smsCertificationUtil;
    private final CoinPaymentHistoryService coinPaymentHistoryService;
    private final CoinService coinService;

    @EventListener
    @Transactional
    public void handle(CoinSuccessEvent event) {
        Coin coin = coinService.createNewCoinByKakaoPay(event.member(), event.dto());
        CoinPaymentHistory history = coinPaymentHistoryService.createNewCoinPayment(event.member(), event.dto(), coin);
        coinService.setCoinAndCoinPayment(coin, history);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handle(CoinPaymentAllSuccessEvent event) {
//        smsCertificationUtil.sendCoinPaymentSMS(event.phoneNumber(), event.quantity(), event.totalAmount());
        kakaoAlimtalkUtil.coinPaymentAlimtalk(event.phoneNumber(), event.totalAmount(), event.quantity() + "코인", event.paymentMethod());
        log.info("{} 에게 문자 발송 완료", event.phoneNumber());
    }

}
