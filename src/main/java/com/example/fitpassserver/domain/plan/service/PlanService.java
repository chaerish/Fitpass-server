package com.example.fitpassserver.domain.plan.service;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PGRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.dto.event.PlanCancelEvent;
import com.example.fitpassserver.domain.plan.dto.event.PlanChangeEvent;
import com.example.fitpassserver.domain.plan.dto.event.RegularSubscriptionEvent;
import com.example.fitpassserver.domain.plan.dto.request.PlanChangeRequestDTO;
import com.example.fitpassserver.domain.plan.dto.response.ChangePlanDTO;
import com.example.fitpassserver.domain.plan.entity.PaymentType;
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
    public void syncPlanStatus(Plan plan) {
        plan.changePlanType(PlanType.NONE);
        planRepository.save(plan);
    }

    public void checkOriginalPlan(Member member) {
        boolean flag = planRepository.existsByMemberAndPlanTypeNotAndPlanTypeIsNotNull(member, PlanType.NONE);
        if (flag) {
            throw new PlanException(PlanErrorCode.PLAN_DUPLICATE_ERROR);
        }
    }

    public Plan createNewKakaoPlan(Member member, String planName, String sid) {
        return this.createNewPlan(member, planName, sid, PaymentType.KAKAO);
    }

    public Plan createNewPGPlan(Member member, PGRequestDTO.PGSubscriptionPaymentWithBillingKeyRequestDTO dto) {
        return this.createNewPlan(member, dto.orderName(), dto.billingKey(), PaymentType.PG);
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
        eventPublisher.publishEvent(new PlanChangeEvent.ChangeUpdateEvent(plan, planType));
        return new ChangePlanDTO(plan, planType);
    }

    @Transactional
    public void changeSubscriptionInfo(Plan plan, PlanType planType) {
        String oldPlanName = plan.getPlanType().getName();
        plan.changePlanType(planType);
        plan.updatePlanDate();
        plan.resetPaymentCount();
        planRepository.save(plan);
        eventPublisher.publishEvent(
                new PlanChangeEvent.ChangeSuccessEvent(plan.getMember().getPhoneNumber(), oldPlanName,
                        plan.getPlanType().getName()));
    }
    
    public void updatePlanInfo(Plan plan, CoinPaymentHistory history) {
        plan.updatePlanSubscriptionInfo();
        planRepository.save(plan);
        eventPublisher.publishEvent(
                new RegularSubscriptionEvent.InfoUpdateSuccessEvent(
                        plan.getPlanType().getName(), plan.getMember().getPhoneNumber(),
                        history.getPaymentPrice(), history.getPaymentMethod()));
    }


    private Plan createNewPlan(Member member, String planName, String sid, PaymentType paymentType) {
        Plan plan = planRepository.findByMember(member).orElse(null);
        if (plan == null) {
            return planRepository.save(Plan.builder()
                    .planType(PlanType.getPlanType(planName))
                    .planDate(LocalDate.now())
                    .sid(sid)
                    .paymentStatus(PaymentStatus.SUCCESS)
                    .paymentType(paymentType)
                    .paymentCount(1)
                    .member(member)
                    .build());
        }
        plan.changePlanType(PlanType.getPlanType(planName));
        planRepository.save(plan);
        return plan;
    }

    @Transactional
    public void cancelPlanByMember(Plan plan) {
        String origin = plan.getPlanType().getName();
        plan.changePlanType(PlanType.NONE);
        planRepository.save(plan);
        eventPublisher.publishEvent(
                new PlanCancelEvent.PlanCancelSuccessEvent(plan.getMember().getPhoneNumber(), origin));
    }

    public Plan getPlan(Member member) {
        return planRepository.findByMember(member).orElseThrow(
                () -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND)
        );
    }
}
