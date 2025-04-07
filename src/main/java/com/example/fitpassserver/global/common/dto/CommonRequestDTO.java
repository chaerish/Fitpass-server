package com.example.fitpassserver.global.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

public class CommonRequestDTO {

    /**
     * 로그인 DTO
     */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDTO {
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        private String loginId;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        private String password;
    }

    /**
     * Refresh Token DTO
     */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefreshRequest {
        @NotBlank(message = "리프레시 토큰은 필수입니다.")
        private String refreshToken;
    }

    /**
     * 중복 아이디 확인 DTO
     */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CheckLoginIdDTO {
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "영어와 숫자를 사용하여 4-12자의 아이디를 입력해주세요.")
        private String loginId;
    }

    /**
     * 공통 회원가입 DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static abstract class JoinDTO {
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "영어와 숫자를 사용하여 4-12자의 아이디를 입력해주세요.")
        protected String loginId;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "영어와 숫자를 사용하여 8-20자의 비밀번호를 입력해주세요.")
        protected String password;

        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        protected String phoneNumber;

        @NotBlank
        protected String name;

        @NotNull
        @JsonProperty("agree")
        protected boolean isAgree;

        @NotNull(message = "필수 동의 사항입니다.")
        @JsonProperty("termsAgreed")
        protected boolean isTermsAgreed;

        @NotNull(message = "필수 동의 사항입니다.")
        @JsonProperty("personalInformationAgreed")
        protected boolean isPersonalInformationAgreed;

        @NotNull(message = "필수 동의 사항입니다.")
        @JsonProperty("thirdPartyAgreed")
        protected boolean isThirdPartyAgreed;

        @NotNull
        @JsonProperty("marketingAgreed")
        protected boolean isMarketingAgreed;
    }
}