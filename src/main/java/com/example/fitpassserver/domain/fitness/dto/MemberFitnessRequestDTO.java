package com.example.fitpassserver.domain.fitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberFitnessRequestDTO {

    @Getter
    @AllArgsConstructor
    public static class CreateMemberFitnessRequestDTO {
        @Schema(name = "fitnessId", description = "구매할 피트니스 ID")
        private Long fitnessId;
        @Schema(name = "agree", description = "구매 약관 동의 여부")
        private boolean agree;
    }

    @Getter
    @AllArgsConstructor
    public static class CancelMemberFitnessRequestDTO {
        @Schema(name = "memberFitnessId", description = "구매 취소할 멤버 피트니스 ID")
        private Long memberFitnessId;
    }
}
