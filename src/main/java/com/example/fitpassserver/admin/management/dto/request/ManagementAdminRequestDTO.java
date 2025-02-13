package com.example.fitpassserver.admin.management.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ManagementAdminRequestDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateCoinManagementDTO {
        @Schema(description = "플랜 이름", example = "BASIC")
        private String name;

        @Schema(description = "월 가격", example = "10000")
        private int price;

        @Schema(description = "지급 코인 개수", example = "100")
        private int coinQuantity;

        @Schema(description = "추가 지급 코인 개수", example = "10")
        private int coinAddition;

        @Schema(description = "유효 기간", example = "30일")
        private String expirationPeriod;
    }
}
