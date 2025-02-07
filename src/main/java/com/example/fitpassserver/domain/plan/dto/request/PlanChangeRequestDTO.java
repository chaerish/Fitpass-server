package com.example.fitpassserver.domain.plan.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PlanChangeRequestDTO(
        @Schema(description = "플랜 이름")
        @NotNull(message = "플랜 이름을 입력해주세요.")
        String planName
) {
}
