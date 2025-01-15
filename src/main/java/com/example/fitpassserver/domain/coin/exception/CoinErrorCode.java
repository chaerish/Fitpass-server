package com.example.fitpassserver.domain.coin.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CoinErrorCode implements BaseErrorCode {
    COIN_NOT_FOUND(HttpStatus.NOT_FOUND, "COIN404", "코인의 단위가 맞지 않습니다."),
    COIN_UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "COIN401", "해당 코인에 대한 권한이 없습니다.");
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
