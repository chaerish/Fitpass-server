package com.example.fitpassserver.domain.plan.dto.request;

import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.plan.entity.Plan;

public record NotificationInfo(
        String phoneNumber,
        String planName,
        String planId,
        int paymentPrice,
        String paymentMethod
) {
    public static NotificationInfo to(String phoneNumber, Plan plan, CoinPaymentHistory history) {
        return new NotificationInfo(
                phoneNumber,
                plan.getPlanType().getName(),
                String.valueOf(plan.getId()),
                history.getPaymentPrice(),
                history.getPaymentMethod()
        );

    }
}