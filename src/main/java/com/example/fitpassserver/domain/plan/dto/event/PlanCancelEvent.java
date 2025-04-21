package com.example.fitpassserver.domain.plan.dto.event;

import com.example.fitpassserver.domain.plan.entity.Plan;

public record PlanCancelEvent(
) {
    public record PlanCancelSuccessEvent(
            String phoneNumber,
            String planName
    ) {
    }

    public record PlanCancelUpdateEvent(
            Plan plan
    ) {
    }
}
