package com.example.fitpassserver.domain.plan.listener;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.kakaoNotice.util.KakaoAlimtalkUtil;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.sms.util.SmsCertificationUtil;
import com.example.fitpassserver.domain.plan.dto.event.PlanCancelEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanChangeAllSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanChangeSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanPaymentAllSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.RegularSubscriptionEvent;
import com.example.fitpassserver.domain.plan.dto.request.NotificationInfo;
import com.example.fitpassserver.domain.plan.dto.response.PlanSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.service.PlanRedisService;
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
    private final PlanRedisService planRedisService;

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
        kakaoAlimtalkUtil.sendCoinOrPlanPayment(event.phoneNumber(), event.planName(), event.paymentMethod());
        log.info("{} 에게 문자 발송 완료", event.phoneNumber());
    }

    @EventListener
    @Transactional
    public void handle(RegularSubscriptionEvent.RegularSubscriptionApprovedEvent event) {
        Plan plan = event.plan();
        Member member = plan.getMember();
        Coin coin = coinService.createSubscriptionNewCoin(member, plan);
        CoinPaymentHistory history = coinPaymentHistoryService.createNewCoinPaymentByScheduler(member, event.dto(),
                coin);
        coinService.setCoinAndCoinPaymentByScheduler(coin, history);
        planService.updatePlanInfoByScheduler(plan, history);
        planRedisService.saveSuccessNotification(plan.getId().toString(),
                NotificationInfo.to(member.getPhoneNumber(), plan, history)); // 알림톡 예약 정보를 Redis에 저장

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
        kakaoAlimtalkUtil.sendPlanChange(event.phoneNumber(), event.oldPlanName(), event.planName());
        log.info("{} 에게 문자 발송 완료", event.phoneNumber());
    }

    /*
    사용자에 의해 취소
     */
    @EventListener
    @Transactional
    public void handle(PlanCancelEvent.PlanCancelUpdateEvent event) {
        planService.cancelPlanByMember(event.plan());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handle(PlanCancelEvent.PlanCancelSuccessEvent event) {
        kakaoAlimtalkUtil.sendSecondPaymentFail(event.phoneNumber());
        log.info("{} 에게 문자 발송 완료", event.phoneNumber());
    }

}
