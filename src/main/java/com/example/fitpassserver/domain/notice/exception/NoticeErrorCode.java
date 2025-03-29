package com.example.fitpassserver.domain.notice.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NoticeErrorCode implements BaseErrorCode {
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND,
            "NOTICE404", "공지사항이 존재하지 않습니다."),
    NOTICE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,
            "NOTICE404", "공지사항의 이미지가 존재하지 않습니다."),
    MEMBER_SLIDE_NOT_CHECKED(HttpStatus.BAD_REQUEST,
            "NOTICE400_1", "사용자 슬라이드 체크박스를 선택하지 않았습니다.")
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