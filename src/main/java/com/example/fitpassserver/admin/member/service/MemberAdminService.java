package com.example.fitpassserver.admin.member.service;

import com.example.fitpassserver.admin.member.dto.response.MemberAdminResponseDTO;
import org.springframework.data.domain.Page;

public interface MemberAdminService {
    MemberAdminResponseDTO.MemberPagesDTO getMembersInfo(int page, int size, String sortBy);
}
