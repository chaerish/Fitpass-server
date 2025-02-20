package com.example.fitpassserver.domain.plan.dto.event;

import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;

public record PlanChangeSuccessEvent(
        Plan plan,
        PlanType planType

) {
}
