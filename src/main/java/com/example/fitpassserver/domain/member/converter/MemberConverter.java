package com.example.fitpassserver.domain.member.converter;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.Role;

import java.time.LocalDateTime;

public class MemberConverter {

    public static MemberResponseDTO.JoinResultDTO toJoinResultDTO(Member member) {
        return MemberResponseDTO.JoinResultDTO.builder()
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .isLocationAgreed(member.isLocationAgreed())
                .build();
    }

    public static Member toMember(MemberRequestDTO.MemberJoinDTO request) {
        Role role = Role.GUEST;
        return Member.builder()
                .loginId(request.getLoginId())
                .name(request.getName())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .isAgree(request.isAgree())
                .isTermsAgreed(request.isTermsAgreed())
                .isPersonalInformaionAgreed(request.isPersonalInformationAgreed())
                .isThirdPartyAgreed(request.isThirdPartyAgreed())
                .isLocationAgreed(request.isLocationAgreed())
                .isMarketingAgreed(request.isMarketingAgreed())
                .isAdditionalInfo(false)
                .build();
    }
}
