package com.example.fitpassserver.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

public class MemberRequestDTO {
    /** 회원가입 dto **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JoinDTO {
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        @Pattern( regexp = "^[a-zA-Z0-9]{4,12}$", message="영어와 숫자를 사용하여 4-12자의 아이디를 입력해주세요.")
        String loginId;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern( regexp = "^[a-zA-Z0-9]{8,20}$", message="영어와 숫자를 사용하여 8-20자의 비밀번호를 입력해주세요.")
        String password;

        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        String phoneNumber;

        @NotBlank
        String name;

        @NotNull
        @JsonProperty("agree")
        boolean isAgree;

        @NotNull(message = "필수 동의 사항입니다.")
        @JsonProperty("termsAgreed")
        boolean isTermsAgreed;

        @NotNull(message = "필수 동의 사항입니다.")
        @JsonProperty("locationAgreed")
        boolean isLocationAgreed;

        @NotNull(message = "필수 동의 사항입니다.")
        @JsonProperty("thirdPartyAgreed")
        boolean isThirdPartyAgreed;

        @NotNull
        @JsonProperty("marketingAgreed")
        boolean isMarketingAgreed;

    }


    /** 로그인 요청 DTO */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDTO {

        String loginId;

        String password;
    }

    /** Refresh Token 요청 DTO */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefreshRequest {
        String refreshToken;
    }

    /** 중복 아이디 확인 요청 dto **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CheckLoginIdDTO {
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        @Pattern( regexp = "^[a-zA-Z0-9]{4,12}$", message="영어와 숫자를 사용하여 4-12자의 아이디를 입력해주세요.")
        String loginId;
    }
    /** 아이디 찾기 dto **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class FindLoginIdDTO{
        @NotNull
        private String name;
        @NotNull
        private String phoneNumber;
    }


}
