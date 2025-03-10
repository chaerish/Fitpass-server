package com.example.fitpassserver.domain.plan.dto.event;

public record PlanPaymentAllSuccessEvent(
        String planName,
        String phoneNumber,
        int totalAmount,
        String paymentMethod
) {
}
