package com.example.fitpassserver.domain.member.service.query;

import com.example.fitpassserver.domain.member.entity.Member;

public interface MemberQueryService {
    Member getMember(String loginId);
}
