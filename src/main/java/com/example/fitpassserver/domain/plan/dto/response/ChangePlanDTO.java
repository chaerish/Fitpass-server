package com.example.fitpassserver.domain.plan.dto.response;

import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;

public record ChangePlanDTO(
        Plan originPlan,
        PlanType changeType
) {
}
