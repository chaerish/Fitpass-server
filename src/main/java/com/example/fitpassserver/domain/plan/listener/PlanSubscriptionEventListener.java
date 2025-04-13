package com.example.fitpassserver.domain.plan.listener;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.kakaoNotice.util.KakaoAlimtalkUtil;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import com.example.fitpassserver.domain.plan.dto.event.PlanCancelSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanCancelUpdateEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanChangeAllSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanChangeSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanPaymentAllSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.RegularSubscriptionApprovedEvent;
import com.example.fitpassserver.domain.plan.dto.response.PlanSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.service.PlanService;
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
public class PlanSubscriptionEventListener {
    private final SmsCertificationUtil smsCertificationUtil;
    private final KakaoAlimtalkUtil kakaoAlimtalkUtil;
    private final CoinPaymentHistoryService coinPaymentHistoryService;
    private final CoinService coinService;
    private final PlanService planService;

    @EventListener
    @Transactional
    public void handle(PlanSuccessEvent event) {
        Member member = event.member();
        PlanSubscriptionResponseDTO dto = event.dto();
        Plan plan = planService.createNewKakaoPlan(member, dto.itemName(), dto.sid());
        Coin coin = coinService.createSubscriptionNewCoin(member, plan);
        CoinPaymentHistory history = coinPaymentHistoryService.createNewCoinPaymentByPlan(event.member(), dto,
                coin);
        coinService.setCoinAndCoinPayment(coin, history);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handle(PlanPaymentAllSuccessEvent event) {
//        smsCertificationUtil.sendPlanPaymentSMS(event.phoneNumber(), event.planName(), event.totalAmount());
        kakaoAlimtalkUtil.coinPaymentAlimtalk(event.phoneNumber(), event.totalAmount(), event.planName(), event.paymentMethod());
        log.info("{} 에게 문자 발송 완료", event.phoneNumber());
    }

    @EventListener
    @Transactional
    public void handle(RegularSubscriptionApprovedEvent event) {
        Plan plan = event.plan();
        Member member = plan.getMember();
        Coin coin = coinService.createSubscriptionNewCoin(member, plan);
        CoinPaymentHistory history = coinPaymentHistoryService.createNewCoinPaymentByScheduler(member, event.dto(),
                coin);
        coinService.setCoinAndCoinPayment(coin, history);
        planService.updatePlanInfo(plan, history);
    }

    @EventListener
    @Transactional
    public void handle(PlanChangeSuccessEvent event) {
        Plan plan = event.plan();
        planService.changeSubscriptionInfo(plan, event.planType());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handle(PlanChangeAllSuccessEvent event) {
//        smsCertificationUtil.sendPlanChangeAlert(event.phoneNumber(), event.planName());
        kakaoAlimtalkUtil.planChangeAlimtalk(event.phoneNumber(), event.planName());
        log.info("{} 에게 문자 발송 완료", event.phoneNumber());
    }

    @EventListener
    @Transactional
    public void handle(PlanCancelUpdateEvent event) {
        planService.cancelNewPlan(event.plan());
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handle(PlanCancelSuccessEvent event) {
//        smsCertificationUtil.sendPlanInActiveAlert(event.phoneNumber(), event.planName());
        kakaoAlimtalkUtil.deactivatePlanAlimtalk(event.phoneNumber(), event.planName());
        log.info("{} 에게 문자 발송 완료", event.phoneNumber());
    }

}
