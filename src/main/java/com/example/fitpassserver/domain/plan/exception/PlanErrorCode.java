package com.example.fitpassserver.domain.plan.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PlanErrorCode implements BaseErrorCode {
    SID_NOT_FOUND(HttpStatus.NOT_FOUND, "SID404", "구독하고 계신 플랜의 SID 를 찾을 수 없습니다."),
    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "Plan404", "구독하고 계신 플랜을 찾을 수 없습니다."),
    PLAN_CHANGE_DUPLICATE_ERROR(HttpStatus.CONFLICT, "Plan409", "이미 해당 플랜을 구독 중입니다."),
    PLAN_DUPLICATE_ERROR(HttpStatus.CONFLICT, "Plan409", "이미 구독하고 계신 플랜이 존재합니다."),
    PLAN_PAYMENT_BAD_REQUEST(HttpStatus.NOT_FOUND, "Plan400", "해당 플랜이 유효하지 않습니다.");
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
