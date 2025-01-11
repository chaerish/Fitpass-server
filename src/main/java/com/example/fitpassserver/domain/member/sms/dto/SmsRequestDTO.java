package com.example.fitpassserver.domain.member.sms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


public class SmsRequestDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CodeSendDTO {
        /** 전화번호 인증 DTO **/
        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        String phoneNumber;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CodeVertifyDTO {
        /** 전화번호 인증 DTO **/
        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        String phoneNumber;
        @NotBlank(message = "인증번호는 필수 입력 값입니다.")
        private String certificationCode;
    }

}
