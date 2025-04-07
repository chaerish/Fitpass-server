package com.example.fitpassserver.owner.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class OwnerResponseDTO {
    /**
     * 회원가입 완료 응답 DTO
     **/
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResultDTO {
        Long ownerId;
        LocalDateTime createdAt;
    }
}
