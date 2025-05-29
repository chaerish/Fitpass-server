package com.example.fitpassserver.domain.plan.dto.event;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.dto.response.PlanSubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.dto.response.SubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.entity.Plan;

public record RegularSubscriptionEvent(
) {
    public record FirstPaymentSuccessEvent(
            Member member,
            PlanSubscriptionResponseDTO dto

    ) {
    }

    public record PaymentSuccessEvent(
            Plan plan,
            SubscriptionResponseDTO dto
    ) {
    }

    public record InfoUpdateSuccessEvent(
            String planName,
            String phoneNumber,
            int totalAmount,
            String paymentMethod
    ) {
    }

}
