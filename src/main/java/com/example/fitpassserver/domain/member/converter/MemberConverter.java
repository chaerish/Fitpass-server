package com.example.fitpassserver.domain.member.converter;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.Role;

import com.example.fitpassserver.global.oauth.provider.OAuth2UserInfo;
import java.time.LocalDateTime;
import java.util.UUID;

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
                .isWork(request.isWork())
                .companyName(request.getCompany_name())
                .isAgree(request.isAgree())
                .isTermsAgreed(request.isTermsAgreed())
                .isPersonalInformationAgreed(request.isPersonalInformationAgreed())
                .isThirdPartyAgreed(request.isThirdPartyAgreed())
                .isLocationAgreed(request.isLocationAgreed())
                .isMarketingAgreed(request.isMarketingAgreed())
                .isAdditionalInfo(false)
                .build();
    }

    public static Member toMember(OAuth2UserInfo oauth2UserInfo) {
        Role role = Role.GUEST;
        return Member.builder()
                .loginId(oauth2UserInfo.getProvider() + "_" + oauth2UserInfo.getProviderId())
                .provider(oauth2UserInfo.getProvider())
                .providerId(oauth2UserInfo.getProviderId())
                .name(oauth2UserInfo.getName())
                .password(UUID.randomUUID().toString())
                .phoneNumber("temp")
                .role(role)
                .isWork(false)
                .isAgree(false)
                .isTermsAgreed(false)
                .isPersonalInformationAgreed(false)
                .isThirdPartyAgreed(false)
                .isLocationAgreed(false)
                .isMarketingAgreed(false)
                .isAdditionalInfo(false)
                .build();
    }
}
