package com.example.fitpassserver.domain.coinPaymentHistory.dto.response;

import lombok.Builder;

@Builder
public record PaymentIdResponse(
        String paymentId
) {
}
