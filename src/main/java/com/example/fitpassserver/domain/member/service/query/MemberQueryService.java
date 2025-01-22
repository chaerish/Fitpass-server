package com.example.fitpassserver.domain.member.service.query;

import com.example.fitpassserver.domain.member.dto.MemberRequestDTO;
import com.example.fitpassserver.domain.member.entity.Member;

public interface MemberQueryService {
    Member getMember(String loginId);

    String getLoginId(MemberRequestDTO.FindLoginIdDTO requst);

    boolean checkLoginId(String loginId);

    String findPassword(MemberRequestDTO.FindPasswordDTO requst);
}
