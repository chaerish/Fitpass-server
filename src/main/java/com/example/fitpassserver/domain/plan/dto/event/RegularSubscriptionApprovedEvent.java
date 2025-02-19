package com.example.fitpassserver.domain.plan.dto.event;

import com.example.fitpassserver.domain.plan.dto.response.SubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.entity.Plan;

public record RegularSubscriptionApprovedEvent(
        Plan plan,
        SubscriptionResponseDTO dto
) {
}
