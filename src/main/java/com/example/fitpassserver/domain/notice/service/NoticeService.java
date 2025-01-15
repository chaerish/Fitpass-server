package com.example.fitpassserver.domain.notice.service;

import com.example.fitpassserver.domain.notice.controller.response.NoticeDetailResponse;
import com.example.fitpassserver.domain.notice.controller.response.NoticeListResponse;
import com.example.fitpassserver.domain.notice.entity.Notice;
import com.example.fitpassserver.domain.notice.repository.NoticeRepository;
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

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public Map<String, Object> getNoticeList(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        List<NoticeListResponse> content = noticePage.getContent().stream()
                .map(notice -> new NoticeListResponse(
                        notice.getId(),
                        notice.getTitle()
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
                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));

        return new NoticeDetailResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getNoticeImage(),
                notice.getCreatedAt()
        );
    }
}