package com.example.fitpassserver.domain.coin.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum CoinUsageHistoryErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "COIN_USAGE400_1", "코인 사용 내역이 없습니다."),
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
