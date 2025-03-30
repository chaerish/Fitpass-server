package com.example.fitpassserver.admin.notice.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NoticeAdminErrorCode implements BaseErrorCode {
    HOME_SLIDE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST,
            "NOTICE400", "홈 슬라이드는 최대 3개까지만 설정할 수 있습니다."),
    HOME_SLIDE_DRAFT_NOT_ALLOWED(HttpStatus.BAD_REQUEST,
            "NOTICE400", "임시저장된 공지사항은 홈 슬라이드에 설정할 수 없습니다."),
    MEMBER_SLIDE_DRAFT_NOT_ALLOWED(HttpStatus.BAD_REQUEST,
            "NOTICE400", "임시저장된 공지사항은 회원 슬라이드에 설정할 수 없습니다."),
    TITLE_REQUIRED(HttpStatus.BAD_REQUEST,
            "NOTICE401", "제목은 필수입니다."),
    IMAGE_REQUIRED(HttpStatus.BAD_REQUEST,
            "NOTICE401","정식 등록 시 이미지는 필수입니다."),
    CONTENT_REQUIRED(HttpStatus.BAD_REQUEST,
            "NOTICE402", "정식 등록 시 내용은 필수입니다."),
    TYPE_REQUIRED(HttpStatus.BAD_REQUEST,
            "NOTICE403", "정식 등록 시 공지 유형(type)은 필수입니다."),
    TITLE_TOO_SHORT(HttpStatus.BAD_REQUEST,
            "NOTICE404", "제목은 최소 5자 이상이어야 합니다.")
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