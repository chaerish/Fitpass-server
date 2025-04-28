package com.example.fitpassserver.domain.plan.service.scheduler;

import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanSchedulerHelperService {
    private final PlanRepository planRepository;

    public List<Plan> findPaymentTarget() {
        return planRepository.findAllByPlanDateLessThanEqualAndPlanTypeIsNot(
                LocalDate.now().minusMonths(1), PlanType.NONE);
    }

    public Plan getPlan(Member member) {
        return planRepository.findByMember(member).orElseThrow(
                () -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND)
        );
    }
    
    @Transactional
    public void cancelPlanByAuto(Plan plan) {
        plan.cancelPlan();
        planRepository.save(plan);
    }

    @Transactional
    public void insufficient(Plan plan) {
        plan.setPaymentStatus(PaymentStatus.INSUFFICIENT);
        planRepository.save(plan);
    }

    public void fail(Plan plan) {
        plan.setPaymentStatus(PaymentStatus.FAIL);
        planRepository.save(plan);
    }
}
