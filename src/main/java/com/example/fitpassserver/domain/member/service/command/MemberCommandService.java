package com.example.fitpassserver.domain.member.service.command;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.entity.Member;

public interface MemberCommandService {
    Member joinMember(MemberRequestDTO.JoinDTO request);
}
