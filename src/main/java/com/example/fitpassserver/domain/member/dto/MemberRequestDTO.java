package com.example.fitpassserver.domain.member.dto;

import com.example.fitpassserver.global.common.dto.CommonRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequestDTO {
    /**
     * 회원가입 dto
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberJoinDTO extends CommonRequestDTO.JoinDTO {
        @NotNull
        @JsonProperty("isWork")
        private boolean isWork;

        private String company_name;

        @NotNull
        @JsonProperty("locationAgreed")
        boolean isLocationAgreed;
    }

    /**
     * 소셜 로그인 후 회원가입 dto
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SocialJoinDTO {
        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        private String phoneNumber;

        @NotBlank
        private String name;

        @NotNull
        @JsonProperty("isWork")
        private boolean isWork;

        private String company_name;

        @NotNull
        @JsonProperty("agree")
        private boolean isAgree;

        @NotNull(message = "필수 동의 사항입니다.")
        @JsonProperty("termsAgreed")
        private boolean isTermsAgreed;

        @NotNull(message = "필수 동의 사항입니다.")
        @JsonProperty("locationAgreed")
        private boolean isLocationAgreed;

        @NotNull(message = "필수 동의 사항입니다.")
        @JsonProperty("thirdPartyAgreed")
        private boolean isThirdPartyAgreed;

        @NotNull
        @JsonProperty("marketingAgreed")
        private boolean isMarketingAgreed;
    }


    /**
     * 로그인 요청 DTO
     */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDTO {

        String loginId;

        String password;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor

    public static class LocationDTO {
        @NotNull(message = "위도는 필수 입력 값입니다.")
        private Double latitude;
        @NotNull(message = "경도는 필수 입력 값입니다.")
        private Double longitude;
    }

    /**
     * 아이디 찾기 dto
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FindLoginIdDTO {
        @NotNull
        private String name;
        @NotNull
        private String phoneNumber;
    }

    /**
     * 비밀번호 찾기 dto
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FindPasswordDTO {
        @NotBlank
        private String loginId;
        @NotBlank
        private String name;
        @NotNull
        private String phoneNumber;
    }

    /**
     * 전화번호 변경 dto
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangePhoneNumberDTO {
        @NotBlank
        private String name;
        @NotBlank
        private String password;
        @NotBlank
        private String newPhoneNumber;
    }

    /**
     * 비밀번호 리셋 dto
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResetPasswordDTO {
        @NotBlank(message = "비밀번호 찾기 api를 통해 반환된 loginId값을 넣어주세요")
        private String loginId;
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "영어와 숫자를 사용하여 8-20자의 비밀번호를 입력해주세요.")
        private String newPassword;
    }

    /**
     * 비밀번호 변경 dto
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangePasswordDTO {
        @NotBlank
        private String password;

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "영어와 숫자를 사용하여 8-20자의 비밀번호를 입력해주세요.")
        private String newPassword;
    }


}
