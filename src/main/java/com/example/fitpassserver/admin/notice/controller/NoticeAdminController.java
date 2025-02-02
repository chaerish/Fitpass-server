package com.example.fitpassserver.admin.notice.controller;

import com.example.fitpassserver.admin.notice.service.NoticeAdminService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/admin/notice")
public class NoticeAdminController {

    private final NoticeAdminService noticeAdminService;

    public NoticeAdminController(NoticeAdminService noticeAdminService) {
        this.noticeAdminService = noticeAdminService;
    }

    // 🔹 공지사항 목록 조회 (검색어 적용)
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllNotices(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Map<String, Object> noticeList = noticeAdminService.getNoticeAdminList(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.onSuccess(noticeList));
    }
    // 🔹 홈 슬라이드 체크박스 업데이트 API
    @PatchMapping("/{noticeId}/home-slide-check")
    public ResponseEntity<ApiResponse<Void>> updateHomeSlide(
            @PathVariable Long noticeId,
            @RequestParam boolean isHomeSlide
    ) {
        noticeAdminService.updateHomeSlideStatus(noticeId, isHomeSlide);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}
