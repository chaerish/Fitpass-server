package com.example.fitpassserver.domain.coinPaymentHistory.dto.event;

public record CoinPaymentAllSuccessEvent(
        String phoneNumber,
        int quantity,
        int totalAmount
) {
}
