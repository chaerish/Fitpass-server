package com.example.fitpassserver.admin.management.dto.response;

import com.example.fitpassserver.domain.coin.entity.CoinType;
import com.example.fitpassserver.domain.coin.entity.CoinTypeEntity;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.entity.PlanTypeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ManagementAdminResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlanInfoDTO {
        @Schema(description = "플랜 이름", example = "BASIC")
        private PlanType planType;

        @Schema(description = "월 가격", example = "10000")
        private int price;

        @Schema(description = "지급 코인 개수", example = "100")
        private int coinQuantity;

        @Schema(description = "추가 지급 코인 개수", example = "10")
        private int coinAddition;

        @Schema(description = "유효 기간", example = "30")
        private int expirationPeriod;

        public PlanInfoDTO(PlanTypeEntity plan) {
            this.planType = plan.getPlanType();
            this.price = plan.getPrice();
            this.coinQuantity = plan.getCoinQuantity();
            this.coinAddition = plan.getCoinAddition();
            this.expirationPeriod = plan.getExpirationPeriod();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CoinInfoDTO {
        @Schema(description = "코인 타입(개수)", example = "COIN_1")
        private CoinType coinType;

        @Schema(description = "코인 이름", example = "1코인")
        private String name;

        @Schema(description = "가격", example = "550")
        private int price;

        @Schema(description = "지급 코인 개수", example = "100")
        private int coinQuantity;

        @Schema(description = "추가 지급 코인 개수", example = "10")
        private int coinAddition;

        @Schema(description = "유효 기간", example = "30")
        private int expirationPeriod;

        public CoinInfoDTO(CoinTypeEntity coin) {
            this.coinType = coin.getCoinType();
            this.name = coin.getName();
            this.price = coin.getPrice();
            this.coinQuantity = coin.getCoinQuantity();
            this.coinAddition = coin.getCoinAddition();
            this.expirationPeriod = coin.getExpirationPeriod();
        }
    }
}
