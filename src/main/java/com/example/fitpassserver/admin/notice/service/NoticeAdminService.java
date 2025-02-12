package com.example.fitpassserver.admin.notice.service;

import com.example.fitpassserver.admin.notice.converter.NoticeAdminConverter;
import com.example.fitpassserver.admin.notice.dto.request.NoticeAdminReqDTO;
import com.example.fitpassserver.admin.notice.dto.response.NoticeAdminResDTO;
import com.example.fitpassserver.admin.notice.exception.NoticeAdminErrorCode;
import com.example.fitpassserver.admin.notice.exception.NoticeAdminException;
import com.example.fitpassserver.domain.notice.entity.Notice;
import com.example.fitpassserver.domain.notice.exception.NoticeErrorCode;
import com.example.fitpassserver.domain.notice.exception.NoticeException;
import com.example.fitpassserver.domain.notice.repository.NoticeRepository;
import com.example.fitpassserver.domain.notice.service.NoticeService;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NoticeAdminService {
    private final NoticeRepository noticeRepository;
    private final NoticeService noticeService;
    private final S3Service s3Service;

    // 🔹 공지사항 목록 조회
    @Transactional(readOnly = true)
    public Map<String, Object> getNoticeAdminList(String keyword, Pageable pageable) {
        Page<Notice> noticePage = keyword != null && !keyword.trim().isEmpty()
                ? noticeRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(keyword, pageable)
                : noticeRepository.findAllByOrderByCreatedAtDesc(pageable);

        return Map.of(
                "totalPages", noticePage.getTotalPages(),
                "totalElements", noticePage.getTotalElements(),
                "currentPage", noticePage.getNumber() + 1,
                "pageSize", noticePage.getSize(),
                "content", NoticeAdminConverter.toNoticeAdminResDTOList(noticePage.getContent(), noticeService)
        );
    }

    // 🔹 홈 슬라이드 업데이트 (최대 3개 제한)
    @Transactional
    public void updateHomeSlideStatus(Long noticeId, boolean isHomeSlide) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

        if (notice.isDraft()) {
            throw new NoticeAdminException(NoticeAdminErrorCode.HOME_SLIDE_DRAFT_NOT_ALLOWED);
        }
        if (isHomeSlide && noticeRepository.countByIsHomeSlideTrue() >= 3) {
            throw new NoticeAdminException(NoticeAdminErrorCode.HOME_SLIDE_LIMIT_EXCEEDED);
        }
        notice.setHomeSlide(isHomeSlide);
        noticeRepository.save(notice);
    }

    // 🔹 공지사항 "임시저장"
    @Transactional
    public NoticeAdminResDTO saveNoticeDraft(MultipartFile mainImage, NoticeAdminReqDTO request) throws IOException {
        Notice notice;
        String imageKey = (mainImage != null && !mainImage.isEmpty()) ? uploadNoticeImage(mainImage) : null;

        if (request.getId() != null) {
            notice = noticeRepository.findById(request.getId())
                    .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

            notice = Notice.builder()
                    .id(notice.getId())
                    .title(request.getTitle() != null ? request.getTitle() : notice.getTitle())
                    .content(request.getContent() != null ? request.getContent() : notice.getContent())
                    .type(request.getType() != null ? request.getType() : notice.getType())
                    .noticeImage(imageKey != null ? imageKey : notice.getNoticeImage())
                    .isDraft(true)
                    .build();
        } else {
            notice = NoticeAdminConverter.toEntity(request);
            notice.setNoticeImage(imageKey);
            notice.setDraft(true);
        }

        Notice savedNotice = noticeRepository.save(notice);
        return NoticeAdminConverter.toNoticeAdminResDTO(savedNotice, noticeService);
    }

    // 🔹 공지사항 "정식 등록"
    @Transactional
    public NoticeAdminResDTO publishNotice(MultipartFile mainImage, NoticeAdminReqDTO request) throws IOException {
        validateNotice(request);

        Notice notice;
        String imageKey = (mainImage != null && !mainImage.isEmpty()) ? uploadNoticeImage(mainImage) : null;

        if (request.getId() != null) {
            notice = noticeRepository.findById(request.getId())
                    .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

            notice = Notice.builder()
                    .id(notice.getId())
                    .title(request.getTitle())
                    .content(request.getContent())
                    .type(request.getType())
                    .noticeImage(imageKey != null ? imageKey : notice.getNoticeImage())
                    .isDraft(false)
                    .build();
        } else {
            notice = NoticeAdminConverter.toEntity(request);
            notice.setNoticeImage(imageKey);
            notice.setDraft(false);
        }

        Notice savedNotice = noticeRepository.save(notice);
        return NoticeAdminConverter.toNoticeAdminResDTO(savedNotice, noticeService);
    }

    // ✅ 필수값 검증 (정식 등록 시)
    private void validateNotice(NoticeAdminReqDTO request) {
        if (StringUtils.isBlank(request.getTitle())) {
            throw new NoticeAdminException(NoticeAdminErrorCode.TITLE_REQUIRED);
        }
        if (StringUtils.isBlank(request.getContent())) {
            throw new NoticeAdminException(NoticeAdminErrorCode.CONTENT_REQUIRED);
        }
        if (request.getType() == null) {
            throw new NoticeAdminException(NoticeAdminErrorCode.TYPE_REQUIRED);
        }
    }

    // 🔹 공지사항 이미지 업로드
    private String uploadNoticeImage(MultipartFile file) throws IOException {
        String key = generateNoticeImageKey(file.getOriginalFilename());
        s3Service.uploadFile(file, key);
        return key;
    }

    // 🔹 공지사항 이미지 키 생성
    private String generateNoticeImageKey(String originalFilename) {
        return String.format("notice/%s/%s", UUID.randomUUID(), originalFilename);
    }
}