package com.example.fitpassserver.domain.member.service.command;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;

public interface MemberCommandService {
    Member joinMember(MemberRequestDTO.JoinDTO request);

    MemberResponseDTO.MemberTokenDTO login(MemberRequestDTO.LoginDTO dto);

    MemberResponseDTO.MemberTokenDTO refreshToken(String refreshToken);

    void deactivateAccount(Member member);

    Member socialJoinMember(MemberRequestDTO.SocialJoinDTO request, String accessToken);

    void setLocation(String loginId, MemberRequestDTO.LocationDTO dto);

    void changePhoneNumber(Member member, MemberRequestDTO.ChangePhoneNumberDTO request);

    void resetPassword(MemberRequestDTO.ResetPasswordDTO request);
}
