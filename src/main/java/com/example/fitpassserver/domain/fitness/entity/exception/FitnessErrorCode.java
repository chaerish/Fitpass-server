package com.example.fitpassserver.domain.fitness.entity.exception;

import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FitnessErrorCode implements BaseErrorCode {
    PASS_NOT_FOUND(HttpStatus.NOT_FOUND,
            "PASS404_1", "해당 패스가 존재하지 않습니다."),
    INVALID_PASS(HttpStatus.BAD_REQUEST,
            "PASS400_1", "유효하지 않은 패스입니다.")
    ;


    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(getStatus())
                .code(getCode())
                .message(getMessage())
                .build();
    }
}
