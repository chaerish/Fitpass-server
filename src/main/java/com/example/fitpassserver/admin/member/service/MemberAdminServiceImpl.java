package com.example.fitpassserver.admin.member.service;

import com.example.fitpassserver.admin.member.converter.MemberAdminConverter;
import com.example.fitpassserver.admin.member.dto.response.MemberAdminResponseDTO;
import com.example.fitpassserver.admin.member.dto.response.MemberAdminResponseDTO.MemberInfosDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAdminServiceImpl implements MemberAdminService {

    private final MemberRepository memberRepository;

    @Override
    public MemberAdminResponseDTO.MemberPagesDTO getMembersInfo(int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return MemberAdminConverter.toPageDTO(memberPage);
    }
}
