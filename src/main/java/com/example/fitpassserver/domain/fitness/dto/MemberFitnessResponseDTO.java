package com.example.fitpassserver.domain.fitness.dto;

import lombok.*;

public class MemberFitnessResponseDTO {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class CreateMemberFitnessResponseDTO {
        private Long memberFitnessId;
    }
}
