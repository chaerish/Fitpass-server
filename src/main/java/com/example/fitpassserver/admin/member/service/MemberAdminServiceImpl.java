package com.example.fitpassserver.admin.member.service;

import com.example.fitpassserver.admin.member.converter.MemberAdminConverter;
import com.example.fitpassserver.admin.member.dto.response.MemberAdminResponseDTO;
import com.example.fitpassserver.admin.member.dto.response.MemberAdminResponseDTO.MemberInfosDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MemberAdminServiceImpl implements MemberAdminService {

    private final MemberRepository memberRepository;

    @Override
    public MemberAdminResponseDTO.MemberPagesDTO getMembersInfo(int page, int size, String searchType, String keyword) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Member> memberPage;

        if (!StringUtils.hasText(searchType) || !StringUtils.hasText(keyword)) {
            memberPage = memberRepository.findAll(pageRequest);
        } else {
            switch (searchType) {
                case "name":
                    memberPage = memberRepository.findByNameContaining(keyword, pageRequest);
                    break;
                case "loginId":
                    memberPage = memberRepository.findByLoginIdContaining(keyword, pageRequest);
                    break;
                case "phoneNumber":
                    memberPage = memberRepository.findByPhoneNumberContaining(keyword, pageRequest);
                    break;
                default:
                    memberPage = memberRepository.findAll(pageRequest);
            }
        }

        return MemberAdminConverter.toPageDTO(memberPage);
    }
}
