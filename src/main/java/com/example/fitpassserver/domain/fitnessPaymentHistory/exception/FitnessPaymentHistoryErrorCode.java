package com.example.fitpassserver.domain.fitnessPaymentHistory.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum FitnessPaymentHistoryErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "PAYMENT_HISTORY404_1", "구매 내역을 찾지 못했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(this.status)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
