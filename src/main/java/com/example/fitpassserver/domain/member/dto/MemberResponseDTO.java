package com.example.fitpassserver.domain.member.dto;

import com.example.fitpassserver.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberResponseDTO {

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
    }

    /**
     * 회원 정보 응답 DTO
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberInfoDTO {
        private String name;
    }

    /**
     * 회원가입 완료 응답 DTO
     **/
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResultDTO {
        Long memberId;
        LocalDateTime createdAt;
    }

}
