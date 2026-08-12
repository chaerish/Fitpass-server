package com.example.fitpassserver.domain.coinPaymentHistory.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum KakaoPayErrorCode implements BaseErrorCode {
    NO_TID_ERROR(HttpStatus.BAD_REQUEST, "TID400", "tid 를 찾을 수 없습니다."),
    ALREADY_SUCCESS_ERROR(HttpStatus.BAD_REQUEST, "KAKAOPAY400", "사용자의 tid 를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "KAKAOPAY404", "사용자의 tid 를 찾을 수 없습니다."),
    ALREADY_READY_PAYMENT(HttpStatus.BAD_REQUEST, "KAKAOPAY_TID400", "이미 요청된 TID입니다. "),
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "KAKAOPAY_AMOUNT400", "결제 승인 금액이 요청 금액과 일치하지 않습니다."),
    PAYMENT_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "KAKAOPAY_HISTORY404", "결제 내역을 찾을 수 없습니다."),
    INVALID_COIN_ISSUE_STATUS(HttpStatus.BAD_REQUEST, "KAKAOPAY_ISSUE400", "코인 지급을 처리할 수 없는 결제 상태입니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(this.status)
                .isSuccess(false) // 에러이므로 항상 false
                .code(this.code)
                .message(this.message)
                .build();
    }
}
