package com.example.fitpassserver.domain.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProfileResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetProfileDTO {
        @NotBlank
        @Schema(description = "프로필 고유 아이디", example = "0")
        private Long id;

        @NotBlank
        @Schema(description = "사용자의 공유 프로필 URL", example = "https://///...")
        private String pictureUrl;

        @NotBlank
        @Schema(description = "사용자의 S3 프로필 사진 경로", example = "/profile/1/...")
        private String pictureKey;

        @NotBlank
        private String name;

        //멤버가 구매한 패스 이름
        private String planType;

    }


}
