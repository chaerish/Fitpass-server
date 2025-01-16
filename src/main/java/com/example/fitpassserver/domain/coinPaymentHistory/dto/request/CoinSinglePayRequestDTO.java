package com.example.fitpassserver.domain.coinPaymentHistory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CoinSinglePayRequestDTO(
        @NotNull(message = "아이템 이름을 입력해주세요.")
        String itemName,
        @Min(value = 1, message = "1개 이상의 수량을 입력해주세요.")
        Integer quantity,
        @Min(value = 550, message = "550원 이상 금액을 입력해주세요.")
        Integer totalAmount,
        @NotNull(message = "결제 수단을 입력해주세요.")
        String methodName
) {
}