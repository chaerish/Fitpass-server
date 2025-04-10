package com.example.fitpassserver.global.common.dto;

import com.example.fitpassserver.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CommonResponseDTO {
    /**
     * 토큰 정보 응답 DTO
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberTokenDTO {
        Role role;
        String accessToken;
        String refreshToken;

        boolean isLocationAgreed;
        private List<Long> fitnessIds;
    }

}
