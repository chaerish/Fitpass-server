package com.example.fitpassserver.domain.plan.service;

import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.dto.request.PlanChangeRequestDTO;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;

    public void checkOriginalPlan(Member member) {
        boolean flag = planRepository.existsByMemberAndPlanTypeNotAndPlanTypeIsNotNull(member, PlanType.NONE);
        if (flag) {
            throw new PlanException(PlanErrorCode.PLAN_DUPLICATE_ERROR);
        }
    }

    public Plan createNewPlan(Member member, String planName, String sid) {
        return planRepository.save(Plan.builder()
                .planType(PlanType.getPlanType(planName))
                .planDate(LocalDate.now())
                .sid(sid)
                .paymentStatus(PaymentStatus.SUCCESS)
                .paymentCount(1)
                .member(member)
                .build());
    }

    @Transactional
    public void cancelNewPlan(Member member) {
        Plan plan = getPlan(member);
        plan.changePlanType(PlanType.NONE);
        planRepository.save(plan);
    }

    public Plan getPlan(Member member) {
        return planRepository.findByMember(member).orElseThrow(
                () -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND)
        );
    }

    public Plan getRegularPaymentPlan(Member member) {
        Plan plan = getPlan(member);
        if (!plan.isRegularPlan()) {
            throw new PlanException(PlanErrorCode.PLAN_PAYMENT_BAD_REQUEST);
        }
        return plan;
    }

    public String changeSubscription(Member member, PlanChangeRequestDTO dto) {
        Plan plan = getPlan(member);
        PlanType planType = PlanType.getPlanType(dto.planName());
        if (planType == null) {
            throw new PlanException(PlanErrorCode.PLAN_NOT_FOUND);
        }
        if (plan.getPlanType().getName().equals(dto.planName())) {
            throw new PlanException(PlanErrorCode.PLAN_CHANGE_DUPLICATE_ERROR);
        }
        plan.changePlanType(planType);
        planRepository.save(plan);
        return planType.getName();
    }
}
