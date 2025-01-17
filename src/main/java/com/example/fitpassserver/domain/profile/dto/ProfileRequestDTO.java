package com.example.fitpassserver.domain.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProfileRequestDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PutProfileDTO {
        @Schema(description = "업데이트할 사용자 프로필 키 경로", example = "untitle.png")
        @JsonProperty("imageUrl")
        private String key;
    }
}
