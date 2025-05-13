package com.example.fitpassserver.domain.coinPaymentHistory.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentSession {
    private String paymentId;
    private Long memberId;
    private String itemId;
    private int price;
}
