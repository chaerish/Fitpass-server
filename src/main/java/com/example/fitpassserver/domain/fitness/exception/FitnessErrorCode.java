package com.example.fitpassserver.domain.fitness.exception;

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
            "PASS400_1", "유효하지 않은 패스입니다."),
    UNAVAILABLE_PASS(HttpStatus.BAD_REQUEST,
            "PASS400_2", "사용할 수 없는 패스입니다."),
    AGREEMENT_NOT_CHECKED(HttpStatus.BAD_REQUEST,
            "PASS400_3", "동의 항목을 체크해야 합니다."),
    USER_MISMATCH(HttpStatus.BAD_REQUEST,
            "PASS400_4", "로그인한 유저와 패스의 유저가 일치하지 않습니다."),
    FITNESS_NOT_FOUND(HttpStatus.NOT_FOUND,
            "FITNESS404_1", "해당 시설이 존재하지 않습니다."),
    FITNESS_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,
            "FITNESS404_1", "해당 시설의 이미지가 존재하지 않습니다."),
    INVALID_SALE_PRICE(HttpStatus.BAD_REQUEST,
                       "FITNESS400_5", "정가, 판매가가 올바르지 않습니다.");

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
