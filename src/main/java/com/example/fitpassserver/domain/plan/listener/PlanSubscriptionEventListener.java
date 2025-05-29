package com.example.fitpassserver.domain.plan.listener;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.kakaoNotice.util.KakaoAlimtalkUtil;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.dto.event.PlanCancelEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanChangeEvent;
import com.example.fitpassserver.domain.plan.dto.event.RegularSubscriptionEvent;
import com.example.fitpassserver.domain.plan.dto.response.PlanSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.planLog.LogType;
import com.example.fitpassserver.domain.plan.service.PlanService;
import com.example.fitpassserver.domain.plan.service.planLog.NotificationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final KakaoAlimtalkUtil kakaoAlimtalkUtil;
    private final CoinPaymentHistoryService coinPaymentHistoryService;
    private final CoinService coinService;
    private final PlanService planService;
    private final NotificationLogService notificationLogService;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    @Transactional
    public void handle(RegularSubscriptionEvent.FirstPaymentSuccessEvent event) {
        Member member = event.member();
        PlanSubscriptionResponseDTO dto = event.dto();
        Plan plan = planService.createNewKakaoPlan(member, dto.itemName(), dto.sid());
        Coin coin = coinService.createSubscriptionNewCoin(member, plan);
        CoinPaymentHistory history = coinPaymentHistoryService.createNewCoinPaymentByPlan(event.member(), dto,
                coin);
        coinService.setCoinAndCoinPayment(coin, history);
    }


    @EventListener
    @Transactional
    public void handle(RegularSubscriptionEvent.PaymentSuccessEvent event) {
        Plan plan = event.plan();
        Member member = plan.getMember();
        Coin coin = coinService.createSubscriptionNewCoin(member, plan);
        CoinPaymentHistory history = coinPaymentHistoryService.createNewCoinPaymentByScheduler(member, event.dto(),
                coin);
        coinService.setCoinAndCoinPaymentByScheduler(coin, history);
        planService.updatePlanInfo(plan, history);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handle(RegularSubscriptionEvent.InfoUpdateSuccessEvent event) {
        try {
            kakaoAlimtalkUtil.sendCoinOrPlanPayment(event.phoneNumber(), event.planName(), event.paymentMethod());
            log.info("{} 에게 문자 발송 완료", event.phoneNumber());
        } catch (Exception e) {
            log.error("결제 성공 알림톡 전송 실패", e);
            notificationLogService.createFailNotification(LogType.SUCCESS);
        }
    }


    @EventListener
    @Transactional
    public void handle(PlanChangeEvent.ChangeUpdateEvent event) {
        Plan plan = event.plan();
        planService.changeSubscriptionInfo(plan, event.planType());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handle(PlanChangeEvent.ChangeSuccessEvent event) {
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
