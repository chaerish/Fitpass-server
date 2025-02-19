package com.example.fitpassserver.admin.notice.service;

import com.example.fitpassserver.admin.notice.converter.NoticeAdminConverter;
import com.example.fitpassserver.admin.notice.dto.request.NoticeAdminReqDTO;
import com.example.fitpassserver.admin.notice.dto.response.AdminNoticeDetailDTO;
import com.example.fitpassserver.admin.notice.dto.response.NoticeAdminResDTO;
import com.example.fitpassserver.admin.notice.dto.response.NoticeDraftResDTO;
import com.example.fitpassserver.admin.notice.exception.NoticeAdminErrorCode;
import com.example.fitpassserver.admin.notice.exception.NoticeAdminException;
import com.example.fitpassserver.domain.notice.entity.Notice;
import com.example.fitpassserver.domain.notice.exception.NoticeErrorCode;
import com.example.fitpassserver.domain.notice.exception.NoticeException;
import com.example.fitpassserver.domain.notice.repository.NoticeRepository;
import com.example.fitpassserver.domain.notice.service.NoticeService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Transactional
    public NoticeAdminResDTO saveNotice(NoticeAdminReqDTO request, MultipartFile image, boolean isDraft) throws IOException {
        validateRequest(request, image, isDraft);

        String imageUrl = (image != null) ? uploadNoticeImage(image) : null;
        Notice notice;

        if (request.getId() != null) {
            // 기존 공지 수정
            notice = noticeRepository.findById(request.getId())
                    .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));
            notice = updateNotice(notice, request, imageUrl, isDraft);
        } else {
            // 새 공지 저장
            notice = createNewNotice(request, imageUrl, isDraft);
        }

        notice = noticeRepository.save(notice); // 🔹 저장된 엔티티를 반환받아야 함
        return NoticeAdminConverter.toNoticeAdminResDTO(notice, noticeService); // 🔹 변환 후 반환
    }


    private void validateRequest(NoticeAdminReqDTO request, MultipartFile image, boolean isDraft) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new NoticeAdminException(NoticeAdminErrorCode.TITLE_REQUIRED);
        }
        if (!isDraft) { // 정식 등록일 때만 추가 검증
            if (request.getContent() == null || request.getContent().isBlank()) {
                throw new NoticeAdminException(NoticeAdminErrorCode.CONTENT_REQUIRED);
            }
            if (request.getType() == null) {
                throw new NoticeAdminException(NoticeAdminErrorCode.TYPE_REQUIRED);
            }
            // 🔹 기존 DB에 저장된 이미지 확인
            boolean hasExistingImage = false;
            if (request.getId() != null) {
                Notice existingNotice = noticeRepository.findById(request.getId()).orElse(null);
                if (existingNotice != null && existingNotice.getNoticeImage() != null) {
                    hasExistingImage = true; // DB에 기존 이미지 있음
                }
            }

            // 🔹 새로운 이미지가 없고, 기존 DB에도 이미지가 없을 경우 에러 발생
            if ((image == null || image.isEmpty()) && !hasExistingImage) {
                throw new NoticeAdminException(NoticeAdminErrorCode.IMAGE_REQUIRED);
            }
        }
    }

    private Notice createNewNotice(NoticeAdminReqDTO request, String imageUrl, boolean isDraft) {
        return Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .noticeImage(imageUrl)
                .isDraft(isDraft)
                .isHomeSlide(false) // 자동으로 false 설정
                .views(0L)
                .build();
    }

    private Notice updateNotice(Notice notice, NoticeAdminReqDTO request, String imageUrl, boolean isDraft) {
        return Notice.builder()
                .id(notice.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .noticeImage(imageUrl != null ? imageUrl : notice.getNoticeImage()) // 기존 이미지 유지
                .isDraft(isDraft)
                .isHomeSlide(false) // 자동으로 false 설정
                .views(notice.getViews()) // 기존 조회수 유지
                .build();
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

    // 임시저장 중인 공지사항 조회
    public List<NoticeDraftResDTO> getDraftNotices() {
        return noticeRepository.findByIsDraftTrueOrderByCreatedAtDesc()
                .stream()
                .map(notice -> new NoticeDraftResDTO(notice.getId(), notice.getTitle()))
                .collect(Collectors.toList());
    }

    public AdminNoticeDetailDTO getAdminNoticeDetail(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));
        String imageUrl = getNoticeImage(notice.getId());

        return new AdminNoticeDetailDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                imageUrl,
                notice.getType().name()
        );
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