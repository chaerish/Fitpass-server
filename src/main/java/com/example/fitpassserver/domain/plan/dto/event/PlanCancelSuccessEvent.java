package com.example.fitpassserver.domain.plan.dto.event;

public record PlanCancelSuccessEvent(
        String phoneNumber,
        String planName

) {
}
