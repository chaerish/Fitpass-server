package com.example.fitpassserver.domain.plan.dto.event;

public record PlanChangeAllSuccessEvent(
        String phoneNumber,
        String planName
) {
}
