package com.example.fitpassserver.domain.plan.service.planLog;

import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanTypeEntity;
import com.example.fitpassserver.domain.plan.entity.planLog.CancelType;
import com.example.fitpassserver.domain.plan.entity.planLog.NotificationLog;
import com.example.fitpassserver.domain.plan.entity.planLog.PlanAttemptLog;
import com.example.fitpassserver.domain.plan.entity.planLog.PlanCancelLog;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanCancelLogRepository;
import com.example.fitpassserver.domain.plan.repository.PlanTypeRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlanCancelLogService {
    private final PlanCancelLogRepository planCancelLogRepository;
    private final PlanTypeRepository planTypeRepository;

    public void create(PlanAttemptLog planAttemptLog, CancelType cancelType) {
        Plan plan = planAttemptLog.getPlan();
        PlanTypeEntity planTypeEntity = planTypeRepository.findByPlanType(plan.getPlanType())
                .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));
        PlanCancelLog planCancelLog = PlanCancelLog.builder()
                .planName(plan.getPlanType().getName())
                .canceledAt(LocalDateTime.now())
                .cancelType(cancelType)
                .member(plan.getMember())
                .planAttemptLog(planAttemptLog)
                .cancelAmount(planTypeEntity.getPrice())
                .build();
        planCancelLogRepository.save(planCancelLog);
    }

    public List<PlanCancelLog> findCancelNotificationTarget() {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);     // 00:00
        return planCancelLogRepository.findByCanceledAtBetweenAndCancelType(start,
                LocalDateTime.now(), CancelType.AUTO);
    }

    @Transactional
    public void addNotificationLog(PlanCancelLog planCancelLog, NotificationLog notificationLog) {
        planCancelLog.setNotificationLog(notificationLog);
        planCancelLogRepository.save(planCancelLog);
    }


}
