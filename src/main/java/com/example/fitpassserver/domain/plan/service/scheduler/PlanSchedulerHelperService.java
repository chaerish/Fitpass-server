package com.example.fitpassserver.domain.plan.service.scheduler;

import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.coinPaymentHistory.service.KakaoPaymentService;
import com.example.fitpassserver.domain.kakaoNotice.util.KakaoAlimtalkUtil;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.entity.planLog.CancelType;
import com.example.fitpassserver.domain.plan.entity.planLog.LogType;
import com.example.fitpassserver.domain.plan.entity.planLog.NotificationLog;
import com.example.fitpassserver.domain.plan.entity.planLog.PlanAttemptLog;
import com.example.fitpassserver.domain.plan.entity.planLog.PlanCancelLog;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import com.example.fitpassserver.domain.plan.service.planLog.NotificationLogService;
import com.example.fitpassserver.domain.plan.service.planLog.PlanAttemptLogService;
import com.example.fitpassserver.domain.plan.service.planLog.PlanCancelLogService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlanSchedulerHelperService {
    private final PlanRepository planRepository;
    private final KakaoPaymentService kakaoPaymentService;
    private final KakaoAlimtalkUtil kakaoAlimtalkUtil;
    private final PlanAttemptLogService planAttemptLogService;
    private final PlanCancelLogService planCancelLogService;
    private final NotificationLogService notificationLogService;

    public List<Plan> findPaymentTarget() {
        return planRepository.findAllByPlanDateLessThanEqualAndPlanTypeIsNot(
                LocalDate.now().minusMonths(1), PlanType.NONE);
    }

    //2번째 시도 취소
    @Transactional
    public void cancelByAuto(Plan plan) {
        PlanAttemptLog attemptLog = planAttemptLogService.updateAttemptFrequency(plan);
        markAsCancel(plan);
        kakaoPaymentService.cancelSubscriptionByAuto(plan);
        PlanCancelLog log = planCancelLogService.create(attemptLog, CancelType.AUTO);
        sentCancelNotification(log);
    }

    private void markAsCancel(Plan plan) {
        plan.cancelPlan();
        planRepository.save(plan);
    }


    @Async
    public void sentCancelNotification(PlanCancelLog target) {
        //취소 알림톡
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
    //첫번째 잔액결제 실패

    @Transactional
    public void handleFirstAttempt(Plan plan, PlanAttemptLog log) {
        plan.setPaymentStatus(PaymentStatus.INSUFFICIENT);
        planRepository.save(plan);
        sentFailNotification(log);
    }

    @Async
    public void sentFailNotification(PlanAttemptLog target) {
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
    //그냥 실패

    public void fail(Plan plan) {
        plan.setPaymentStatus(PaymentStatus.FAIL);
        planRepository.save(plan);
    }
}
