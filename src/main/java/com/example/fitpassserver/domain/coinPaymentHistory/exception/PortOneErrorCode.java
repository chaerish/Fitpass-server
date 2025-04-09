package com.example.fitpassserver.domain.coinPaymentHistory.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum PortOneErrorCode implements BaseErrorCode {
    PORT_ONE_ERROR_CODE(HttpStatus.BAD_REQUEST, "PORT_ONE400", "PortOne 실행중 에러 발생")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO.builder()
                .httpStatus(this.status)
                .isSuccess(false) // 에러이므로 항상 false
                .code(this.code)
                .message(this.message)
                .build();
    }
}
