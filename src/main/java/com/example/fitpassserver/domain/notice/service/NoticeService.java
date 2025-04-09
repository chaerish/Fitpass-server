package com.example.fitpassserver.domain.notice.service;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.Role;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.domain.notice.controller.response.NoticeDetailResponse;
import com.example.fitpassserver.domain.notice.controller.response.NoticeHomeSlideResponse;
import com.example.fitpassserver.domain.notice.controller.response.NoticeListResponse;
import com.example.fitpassserver.domain.notice.entity.Notice;
import com.example.fitpassserver.domain.notice.exception.NoticeErrorCode;
import com.example.fitpassserver.domain.notice.exception.NoticeException;
import com.example.fitpassserver.domain.notice.repository.NoticeRepository;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;


    // 권한 있는 역할(ADMIN, OWNER)인지 확인하는 메서드
    private boolean isPrivilegedRole(Role role) {
        return role.equals(Role.ADMIN) || role.equals(Role.OWNER);
    }


    public Map<String, Object> getNoticeList(Pageable pageable, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(MemberErrorCode.NOT_FOUND));

        Page<Notice> noticePage = noticeRepository.findPublishedNoticesByIsMemberSlideTrue(pageable);

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

    public NoticeDetailResponse getNoticeDetail(Long noticeId) {

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

        String imageUrl = getNoticeImage(notice.getId());

        // 🔹 조회수 증가
        notice.increaseViews();
        noticeRepository.save(notice);

        return new NoticeDetailResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt(),
                imageUrl,
                notice.getViews()
        );
    }

    //홈슬라이드 게시할 공지사항 조회
    @Transactional(readOnly = true)
    public List<NoticeHomeSlideResponse> getNoticeHomeSlides() {
        List<Notice> homeSlideNotices = noticeRepository.findNoticeHomeSlideIsMemberSlideTrue();

        return homeSlideNotices.stream()
                .map(notice -> new NoticeHomeSlideResponse(
                        notice.getId(),
                        getNoticeImage(notice.getId())
                ))
                .collect(Collectors.toList());
    }

    public String getNoticeImage(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_IMAGE_NOT_FOUND));

        if (notice.getNoticeImage() != null && !notice.getNoticeImage().equals("none")) {
            return s3Service.getGetS3Url(noticeId, notice.getNoticeImage()).getPreSignedUrl();
        } else {
            return "none";
        }
    }
}