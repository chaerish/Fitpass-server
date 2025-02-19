package com.example.fitpassserver.domain.plan.dto.event;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.dto.response.PlanSubscriptionResponseDTO;

public record PlanSuccessEvent(
        Member member,
        PlanSubscriptionResponseDTO dto

) {
}
