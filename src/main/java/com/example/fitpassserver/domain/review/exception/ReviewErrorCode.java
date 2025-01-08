package com.example.fitpassserver.domain.review.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {
    REVIEW_POLICY_NOT_AGREED(HttpStatus.BAD_REQUEST,
            "REVIEW400_1", "리뷰 정책에 동의가 필요합니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND,
            "REVIEW404_1", "해당 리뷰가 존재하지 않습니다."),
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
