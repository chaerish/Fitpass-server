package com.example.fitpassserver.owner.notice.service;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.Role;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.domain.notice.controller.response.NoticeListResponse;
import com.example.fitpassserver.domain.notice.entity.Notice;
import com.example.fitpassserver.domain.notice.exception.NoticeErrorCode;
import com.example.fitpassserver.domain.notice.exception.NoticeException;
import com.example.fitpassserver.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeOwnerServiceImpl implements NoticeOwnerService {
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    private boolean isMemberOwner(Member member) {
        boolean result = false;
        if(member.getRole().equals(Role.OWNER) || member.getRole().equals(Role.ADMIN)){
            result = true;
        }
        else{
            throw new NoticeException(NoticeErrorCode.MEMBER_SLIDE_NOT_CHECKED);
        }
        return result;
    }

    @Override
    public Map<String, Object> getNoticeList(Pageable pageable, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(MemberErrorCode.NOT_FOUND));

        isMemberOwner(member);

        Page<Notice> noticePage = noticeRepository.findPublishedNoticesSorted(pageable);

        List<NoticeListResponse> content = noticePage.getContent().stream()
                .map(notice -> new NoticeListResponse(
                        notice.getId(),
                        notice.getTitle(),
                        notice.getType().getValue()
                ))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("totalPages", noticePage.getTotalPages());
        result.put("totalElements", noticePage.getTotalElements());
        result.put("content", content);
        return result;
    }
}
