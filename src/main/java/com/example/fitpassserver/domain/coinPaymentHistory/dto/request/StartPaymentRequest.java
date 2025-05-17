package com.example.fitpassserver.domain.coinPaymentHistory.dto.request;

public record StartPaymentRequest(
        String itemId,
        int price
) {
}
