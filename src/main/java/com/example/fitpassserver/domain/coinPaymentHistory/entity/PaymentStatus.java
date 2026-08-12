package com.example.fitpassserver.domain.coinPaymentHistory.entity;

public enum PaymentStatus {
    READY("결제 진행 중"),
    SUCCESS("결제 성공"),
    PAY_SUCCESS("카카오페이 결제 승인 완료"),
    COIN_ISSUE_FAILED("코인 지급 실패"),
    CANCEL("결제 취소"),
    INSUFFICIENT("잔액 부족"),
    FAIL("결제 실패");
    private String message;

    PaymentStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
