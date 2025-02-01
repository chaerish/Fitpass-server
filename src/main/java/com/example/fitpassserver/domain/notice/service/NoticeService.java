package com.example.fitpassserver.domain.notice.service;

import com.example.fitpassserver.domain.notice.controller.response.NoticeDetailResponse;
import com.example.fitpassserver.domain.notice.controller.response.NoticeListResponse;
import com.example.fitpassserver.domain.notice.entity.Notice;
import com.example.fitpassserver.domain.notice.exception.NoticeErrorCode;
import com.example.fitpassserver.domain.notice.exception.NoticeException;
import com.example.fitpassserver.domain.notice.repository.NoticeRepository;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final S3Service s3Service;

    public NoticeService(NoticeRepository noticeRepository, S3Service s3Service) {
        this.noticeRepository = noticeRepository;
        this.s3Service = s3Service;
    }

    public Map<String, Object> getNoticeList(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        List<NoticeListResponse> content = noticePage.getContent().stream()
                .map(notice -> new NoticeListResponse(
                        notice.getId(),
                        notice.getTitle(),
                        notice.getType()
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

        return new NoticeDetailResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt(),
                imageUrl
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