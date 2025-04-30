package com.example.fitpassserver.domain.plan.service.planLog;

import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.planLog.NotificationLog;
import com.example.fitpassserver.domain.plan.entity.planLog.PlanAttemptLog;
import com.example.fitpassserver.domain.plan.repository.PlanAttemptLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlanAttemptLogService {
    private final PlanAttemptLogRepository planAttemptLogRepository;

    //오늘 첫번째 시도가 있었는가?
    public boolean existsTodayAttempt(Plan plan) {
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime now = LocalDateTime.now();
        return planAttemptLogRepository.existsByPlanAndCreatedAtBetween(plan, todayStart, now);
    }

    /*
    2번 결제 시도를 했으면 마침내 취소
     */
    @Transactional
    public PlanAttemptLog updateAttemptFrequency(Plan plan) {
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime now = LocalDateTime.now();
        PlanAttemptLog attemptLog = planAttemptLogRepository.findByPlanAndCreatedAtBetween(plan, todayStart, now);
        attemptLog.updateAttemptFrequency();
        planAttemptLogRepository.save(attemptLog);
        return attemptLog;
    }

    public PlanAttemptLog createFirstAttemptLog(Plan plan) {
        PlanAttemptLog planAttemptLog = PlanAttemptLog.builder()
                .plan(plan)
                .firstAttemptedAt(LocalDateTime.now())
                .attemptOrder(1)
                .isCompleted(false)
                .build();
        return planAttemptLogRepository.save(planAttemptLog);
    }

    public List<PlanAttemptLog> findFailureNotificationTarget() {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);     // 00:00
        return planAttemptLogRepository.findFailureNotificationTarget(start,
                LocalDateTime.now());
    }

    @Transactional
    public void addNotificationLog(PlanAttemptLog attemptLog, NotificationLog notificationLog) {
        attemptLog.setNotificationLog(notificationLog);
        planAttemptLogRepository.save(attemptLog);
    }

}
