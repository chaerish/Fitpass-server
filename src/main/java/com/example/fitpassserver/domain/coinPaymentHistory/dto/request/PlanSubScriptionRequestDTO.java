package com.example.fitpassserver.domain.coinPaymentHistory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PlanSubScriptionRequestDTO(
        @Schema(description = "패스 이름")
        @NotNull(message = "패스 이름을 입력해주세요.")
        String itemName,
        @Schema(description = "총 금액")
        @Min(value = 50000, message = "50000원 이상 금액을 입력해주세요.")
        Integer totalAmount,
        @Schema(description = "결제 수단")
        @NotNull(message = "결제 수단을 입력해주세요.")
        String methodName,
        String tid
) {
}