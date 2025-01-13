package com.example.fitpassserver.domain.fitness.entity.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum MemberFitnessErrorCode implements BaseErrorCode {

    MEMBER_FITNESS_POLICY_NOT_AGREE(HttpStatus.BAD_REQUEST, "PASS400_1", "패스에 대한 정책 동의가 필요합니다."),
    NOT_ENOUGH_COIN(HttpStatus.BAD_REQUEST, "PASS400_2", "패스 구매에 필요한 코인이 부족합니다.");
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
