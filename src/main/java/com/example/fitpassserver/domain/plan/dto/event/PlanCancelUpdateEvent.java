package com.example.fitpassserver.domain.plan.dto.event;

import com.example.fitpassserver.domain.plan.entity.Plan;

public record PlanCancelUpdateEvent(
        Plan plan
) {
}
