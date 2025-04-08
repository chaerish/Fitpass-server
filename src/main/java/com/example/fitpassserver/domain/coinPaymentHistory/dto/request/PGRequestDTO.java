package com.example.fitpassserver.domain.coinPaymentHistory.dto.request;

public class PGRequestDTO {
    public record PGSinglePayRequestDTO(
            String paymentId,
            String txId
    ) {
    }

    public record PGPaymentWithBillingKeyRequestDTO(
            String billingKey,
            String orderName,
            int amount
    ) {
    }
}
