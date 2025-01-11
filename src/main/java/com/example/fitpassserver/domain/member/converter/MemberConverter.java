package com.example.fitpassserver.domain.member.converter;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.Role;

import java.time.LocalDateTime;

public class MemberConverter {

    public static MemberResponseDTO.JoinResultDTO toJoinResultDTO(Member member){
        return MemberResponseDTO.JoinResultDTO.builder()
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Member toMember(MemberRequestDTO.JoinDTO request) {
        Role role=Role.GUEST;

        return Member.builder()
                .loginId(request.getLoginId())
                .name(request.getName())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .isAgree(request.isAgree())
                .isTermsAgreed(request.isTermsAgreed())
                .isLocationAgreed(request.isLocationAgreed())
                .isThirdPartyAgreed(request.isThirdPartyAgreed())
                .isMarketingAgreed(request.isMarketingAgreed())
                .build();
    }
}
