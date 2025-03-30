package com.example.fitpassserver.domain.notice.controller;

import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.notice.controller.response.NoticeDetailResponse;
import com.example.fitpassserver.domain.notice.controller.response.NoticeHomeSlideResponse;
import com.example.fitpassserver.domain.notice.service.NoticeService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notice")
@Tag(name = "공지 API")
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }
    @Operation(summary = "공지 목록 조회", description = "공지 목록 전체를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNoticeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @CurrentMember Member member) {
        Map<String, Object> noticePage = noticeService.getNoticeList(PageRequest.of(page, size), member.getId());
        return ResponseEntity.ok(ApiResponse.onSuccess(noticePage));
    }
    @Operation(summary = "공지 상세 조회", description = "공지사항 하나를 상세 조회합니다.")
    @GetMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeDetailResponse>> getNoticeDetail(@PathVariable Long noticeId) {
        NoticeDetailResponse noticeDetail = noticeService.getNoticeDetail(noticeId);
        return ResponseEntity.ok(ApiResponse.onSuccess(noticeDetail));
    }
    @Operation(summary = "홈슬라이드 공지 이미지 조회", description = "홈 슬라이드에 표시될 공지사항 이미지를 조회합니다(최대 3개).")
    @GetMapping("/homeSlide")
    public ResponseEntity<ApiResponse<List<NoticeHomeSlideResponse>>> getNoticeHomeSlides(@CurrentMember Member member) {
        List<NoticeHomeSlideResponse> homeSlides = noticeService.getNoticeHomeSlides(member.getId());
        return ResponseEntity.ok(ApiResponse.onSuccess(homeSlides));
    }
}