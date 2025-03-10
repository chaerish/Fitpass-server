package com.example.fitpassserver.domain.plan.service;

import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.dto.event.PlanCancelSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanChangeAllSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanChangeSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanPaymentAllSuccessEvent;
import com.example.fitpassserver.domain.plan.dto.request.PlanChangeRequestDTO;
import com.example.fitpassserver.domain.plan.dto.response.ChangePlanDTO;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final ApplicationEventPublisher eventPublisher;

    public boolean checkValidPlan(Member member) {
        return planRepository.existsByMemberAndPlanTypeNotAndPlanTypeIsNotNull(member, PlanType.NONE);
    }

    @Transactional
    public void syncPlanStatus(Plan plan, boolean flag) {
        if (!flag && plan.isRegularPlan()) {
            plan.changePlanType(PlanType.NONE);
            planRepository.save(plan);
        }
    }

    public void checkOriginalPlan(Member member) {
        boolean flag = planRepository.existsByMemberAndPlanTypeNotAndPlanTypeIsNotNull(member, PlanType.NONE);
        if (flag) {
            throw new PlanException(PlanErrorCode.PLAN_DUPLICATE_ERROR);
        }
    }

    public Plan createNewPlan(Member member, String planName, String sid) {
        Plan plan = planRepository.findByMember(member).orElse(null);
        if (plan == null) {
            return planRepository.save(Plan.builder()
                    .planType(PlanType.getPlanType(planName))
                    .planDate(LocalDate.now())
                    .sid(sid)
                    .paymentStatus(PaymentStatus.SUCCESS)
                    .paymentCount(1)
                    .member(member)
                    .build());
        }
        plan.changePlanType(PlanType.getPlanType(planName));
        planRepository.save(plan);
        return plan;
    }

    @Transactional
    public void cancelNewPlan(Plan plan) {
        String origin = plan.getPlanType().getName();
        plan.changePlanType(PlanType.NONE);
        planRepository.save(plan);
        eventPublisher.publishEvent(new PlanCancelSuccessEvent(plan.getMember().getPhoneNumber(), origin));
    }

    public Plan getPlan(Member member) {
        return planRepository.findByMember(member).orElseThrow(
                () -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND)
        );
    }

    public Plan checkSubscriptionAndGetPlan(Member member) {
        Plan plan = planRepository.findByMemberAndPlanTypeIsNot(member, PlanType.NONE).orElseThrow(
                () -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND)
        );
        if (!plan.isRegularPlan()) {
            throw new PlanException(PlanErrorCode.SUBSCRIPTION_ALREADY_STOP);
        }
        return plan;
    }

    @Transactional
    public ChangePlanDTO getChangeSubscription(Member member, PlanChangeRequestDTO dto) {
        if (!checkValidPlan(member)) {
            throw new PlanException(PlanErrorCode.PLAN_NOT_FOUND);
        }
        Plan plan = getPlan(member);
        PlanType planType = PlanType.getPlanType(dto.planName());
        if (planType == null) {
            throw new PlanException(PlanErrorCode.PLAN_NOT_FOUND);
        }
        if (plan.getPlanType().getName().equals(dto.planName())) {
            throw new PlanException(PlanErrorCode.PLAN_CHANGE_DUPLICATE_ERROR);
        }
        eventPublisher.publishEvent(new PlanChangeSuccessEvent(plan, planType));
        return new ChangePlanDTO(plan, planType);
    }

    @Transactional
    public void changeSubscriptionInfo(Plan plan, PlanType planType) {
        plan.changePlanType(planType);
        plan.updatePlanDate();
        plan.resetPaymentCount();
        planRepository.save(plan);
        eventPublisher.publishEvent(
                new PlanChangeAllSuccessEvent(plan.getMember().getPhoneNumber(), plan.getPlanType().getName()));
    }


    public void updatePlanInfo(Plan plan, CoinPaymentHistory history) {
        plan.updatePlanSubscriptionInfo();
        planRepository.save(plan);
        eventPublisher.publishEvent(
                new PlanPaymentAllSuccessEvent(plan.getMember().getPhoneNumber(), plan.getPlanType().getName(),
                        history.getPaymentPrice(), history.getPaymentMethod()));
    }
}
