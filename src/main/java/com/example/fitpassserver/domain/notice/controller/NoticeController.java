package com.example.fitpassserver.domain.notice.controller;

import com.example.fitpassserver.domain.notice.controller.response.NoticeDetailResponse;
import com.example.fitpassserver.domain.notice.service.NoticeService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/notice")
@Tag(name = "공지 API")
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNoticeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> noticePage = noticeService.getNoticeList(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.onSuccess(noticePage));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeDetailResponse>> getNoticeDetail(@PathVariable Long noticeId) {
        NoticeDetailResponse noticeDetail = noticeService.getNoticeDetail(noticeId);
        return ResponseEntity.ok(ApiResponse.onSuccess(noticeDetail));
    }
}