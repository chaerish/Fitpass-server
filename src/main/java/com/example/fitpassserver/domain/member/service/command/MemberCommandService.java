package com.example.fitpassserver.domain.member.service.command;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.dto.MemberResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import org.springframework.security.core.Authentication;

public interface MemberCommandService {
    Member joinMember(MemberRequestDTO.JoinDTO request);
    MemberResponseDTO.MemberTokenDTO login(MemberRequestDTO.LoginDTO dto);
    MemberResponseDTO.MemberTokenDTO refreshToken(String refreshToken);
    void deactivateAccount(Long memberId);
    Member socialJoinMember(MemberRequestDTO.SocialJoinDTO request, String accessToken);
}
