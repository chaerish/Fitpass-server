package com.example.fitpassserver.domain.fitness.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum MemberFitnessErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "PASS404_1","구매한 패스를 찾지 못했습니다."),
    MEMBER_FITNESS_POLICY_NOT_AGREE(HttpStatus.BAD_REQUEST, "PASS400_1", "패스에 대한 정책 동의가 필요합니다."),
    NOT_ENOUGH_COIN(HttpStatus.BAD_REQUEST, "PASS400_2", "패스 구매에 필요한 코인이 부족합니다."),
    EXIST_PASS(HttpStatus.BAD_REQUEST, "PASS400_3", "이미 구매한 패스가 존재합니다."),
    NOT_IN_PROGRESS_PASS(HttpStatus.BAD_REQUEST, "PASS400_4", "패스가 활성화 상태가 아닙니다."),
    ALREADY_IN_PROGRESS(HttpStatus.BAD_REQUEST, "PASS_400_5", "패스가 이미 활성화되었습니다."),
    ALREADY_USED_PASS(HttpStatus.BAD_REQUEST, "PASS400_6", "이미 사용이 완료된 패스입니다."),
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
