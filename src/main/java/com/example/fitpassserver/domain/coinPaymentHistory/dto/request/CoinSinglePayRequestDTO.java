package com.example.fitpassserver.domain.coinPaymentHistory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CoinSinglePayRequestDTO(
        @Schema(description = "아이템 이름")
        @NotNull(message = "아이템 이름을 입력해주세요.")
        String itemName,
        @Schema(description = "아이템 수량")
        @Min(value = 1, message = "1개 이상의 수량을 입력해주세요.")
        Integer quantity,
        @Schema(description = "총 금액")
        @Min(value = 550, message = "550원 이상 금액을 입력해주세요.")
        Integer totalAmount,
        @Schema(description = "결제 수단")
        @NotNull(message = "결제 수단을 입력해주세요.")
        String methodName,
        String tid
) {
}