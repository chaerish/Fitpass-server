package com.example.fitpassserver.domain.member.service.command;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.entity.Member;

public interface MemberCommandService {
    Member joinMember(MemberRequestDTO.MemberJoinDTO request);

    Member socialJoinMember(MemberRequestDTO.SocialJoinDTO request, String accessToken);

    void setLocation(String loginId, MemberRequestDTO.LocationDTO dto);

    void updateIsLocationAgree(String loginId);

    void changePhoneNumber(Member member, MemberRequestDTO.ChangePhoneNumberDTO request);

    void changePassword(Member member, MemberRequestDTO.ChangePasswordDTO request);

    void deactivateAccount(Member member);
}
