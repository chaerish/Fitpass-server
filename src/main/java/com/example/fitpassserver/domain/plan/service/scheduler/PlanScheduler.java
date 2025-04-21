package com.example.fitpassserver.domain.plan.service.scheduler;

import com.example.fitpassserver.domain.coinPaymentHistory.service.KakaoPaymentService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.command.PGPaymentCommandService;
import com.example.fitpassserver.domain.kakaoNotice.util.KakaoAlimtalkUtil;
import com.example.fitpassserver.domain.plan.entity.PaymentType;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.planLog.CancelType;
import com.example.fitpassserver.domain.plan.entity.planLog.LogType;
import com.example.fitpassserver.domain.plan.entity.planLog.NotificationLog;
import com.example.fitpassserver.domain.plan.entity.planLog.PlanAttemptLog;
import com.example.fitpassserver.domain.plan.entity.planLog.PlanCancelLog;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.service.planLog.NotificationLogService;
import com.example.fitpassserver.domain.plan.service.planLog.PlanAttemptLogService;
import com.example.fitpassserver.domain.plan.service.planLog.PlanCancelLogService;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlanScheduler {
    private final PlanSchedulerHelperService schedulerHelper;
    private final KakaoPaymentService kakaoPaymentService;
    private final PGPaymentCommandService paymentCommandService;
    private final KakaoAlimtalkUtil kakaoAlimtalkUtil;
    private final PlanAttemptLogService planAttemptLogService;
    private final PlanCancelLogService planCancelLogService;
    private final NotificationLogService notificationLogService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void regularPay() {
        List<Plan> planToProcess = schedulerHelper.findPaymentTarget();
        for (Plan plan : planToProcess) {
            if (plan.getPaymentType().equals(PaymentType.KAKAO)) {
                kakaoSubscriptionPay(plan, this::handleException);
            } else if (plan.getPaymentType().equals(PaymentType.PG)) {
                pgSubscriptionPay(plan, this::handleException);
            }
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    @Async
    public void sentFailNotification() {
        //실패 알림톡
        List<PlanAttemptLog> failureNotificationTarget = planAttemptLogService.findFailureNotificationTarget();
        for (PlanAttemptLog target : failureNotificationTarget) {
            NotificationLog notificationLog;
            try {
                kakaoAlimtalkUtil.sendFirstPaymentFail(target.getPlan().getMember().getPhoneNumber());
                notificationLog = notificationLogService.createNewNotification(LogType.FAIL);
            } catch (Exception e) {
                log.error("결제 실패 알림톡 전송 실패", e);
                notificationLog = notificationLogService.createFailNotification(LogType.FAIL);
            }
            planAttemptLogService.addNotificationLog(target, notificationLog);
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    @Async
    public void sentCancelNotification() {
        //취소 알림톡
        List<PlanCancelLog> cancelNotificationTarget = planCancelLogService.findCancelNotificationTarget();
        for (PlanCancelLog target : cancelNotificationTarget) {
            PlanAttemptLog attemptLog = target.getPlanAttemptLog();
            NotificationLog notificationLog;
            try {
                kakaoAlimtalkUtil.sendSecondPaymentFail(attemptLog.getPlan().getMember().getPhoneNumber());
                notificationLog = notificationLogService.createNewNotification(LogType.CANCEL);
            } catch (Exception e) {
                log.error("결제 취소 알림톡 전송 실패", e);
                notificationLog = notificationLogService.createFailNotification(LogType.CANCEL);
            }
            planCancelLogService.addNotificationLog(target, notificationLog);
        }
    }

    private void kakaoSubscriptionPay(Plan plan, BiConsumer<Plan, PlanException> handleException) {
        try {
            kakaoPaymentService.subscribe(plan);
        } catch (PlanException e) {
            handleException.accept(plan, e);
        } catch (Exception e) {
            log.error("plan ID: {} - 예상치 못한 결제 오류 발생", plan.getId(), e);
        }
    }

    private void pgSubscriptionPay(Plan plan, BiConsumer<Plan, PlanException> handleException) {
        try {
            paymentCommandService.paySubscription(plan);
        } catch (PlanException e) {
            handleException.accept(plan, e);
        } catch (Exception e) {
            log.error("plan ID: {} - 예상치 못한 결제 오류 발생", plan.getId(), e);
        }
    }

    private void handleException(Plan plan, PlanException e) {
        if (e.getBaseErrorCode() == PlanErrorCode.PLAN_INSUFFICIENT_FUNDS) {
            log.error("plan ID: {} - 잔액 부족으로 결제 실패: {}", plan.getId(), e.getMessage(), e);
            boolean alreadyAttempted = planAttemptLogService.existsTodayAttempt(plan); //먼저 기존 attemptLog가 있는지 확인함
            if (alreadyAttempted) {
                cancelByAuto(plan);
            } else {
                planAttemptLogService.createFirstAttemptLog(plan);
                schedulerHelper.insufficient(plan);
            }
        } else {
            planAttemptLogService.createFirstAttemptLog(plan);
            schedulerHelper.fail(plan);
            log.error("plan ID: {} - 결제사 결제 오류", plan.getId(), e);
        }
    }

    @Transactional
    public void cancelByAuto(Plan plan) {
        PlanAttemptLog attemptLog = planAttemptLogService.updateAttemptFrequency(plan);
        schedulerHelper.cancelPlanByAuto(plan);
        kakaoPaymentService.cancelSubscriptionByAuto(plan);
        planCancelLogService.create(attemptLog, CancelType.AUTO);
    }

}
