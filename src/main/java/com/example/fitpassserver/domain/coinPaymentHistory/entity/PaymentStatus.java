package com.example.fitpassserver.domain.coinPaymentHistory.entity;

public enum PaymentStatus {
    SUCCESS("결제 성공"),
    CANCEL("결제 취소"),
    FAIL("결제 실패");
    private String message;

    PaymentStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
