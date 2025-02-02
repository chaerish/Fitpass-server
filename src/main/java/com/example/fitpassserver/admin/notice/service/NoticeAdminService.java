package com.example.fitpassserver.admin.notice.service;

import com.example.fitpassserver.admin.notice.dto.response.NoticeAdminResDTO;
import com.example.fitpassserver.domain.notice.entity.Notice;
import com.example.fitpassserver.domain.notice.repository.NoticeRepository;
import com.example.fitpassserver.domain.notice.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NoticeAdminService {
    private final NoticeRepository noticeRepository;
    private final NoticeService noticeService;

    public NoticeAdminService(NoticeRepository noticeRepository, NoticeService noticeService) {
        this.noticeRepository = noticeRepository;
        this.noticeService = noticeService;
    }
    // 공지사항 목록 조회
    @Transactional(readOnly = true)
    public Map<String, Object> getNoticeAdminList(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<NoticeAdminResDTO> noticeList = noticePage.getContent().stream()
                .map(notice -> new NoticeAdminResDTO(
                        notice.getId(),
                        noticeService.getNoticeImage(notice.getId()),
                        notice.getTitle(),
                        notice.getType().getValue(),
                        notice.getCreatedAt().toLocalDate(),
                        notice.isDraft() ? "임시저장" : "게시중",
                        notice.isHomeSlide()
                ))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", noticePage.getTotalPages());
        response.put("totalElements", noticePage.getTotalElements());
        response.put("currentPage", noticePage.getNumber()+ 1);
        response.put("pageSize", noticePage.getSize());
        response.put("content", noticeList);

        return response;
    }
}
