package com.example.fitpassserver.domain.plan.dto.event;

import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;

public record PlanChangeEvent(

) {
    public record ChangeUpdateEvent(
            Plan plan,
            PlanType planType

    ) {
    }

    public record ChangeSuccessEvent(
            String phoneNumber,
            String oldPlanName,
            String planName
    ) {
    }

}
