package com.example.fitpassserver.owner.notice.controller;

import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.notice.controller.response.NoticeDetailResponse;
import com.example.fitpassserver.domain.notice.service.NoticeService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.owner.notice.service.NoticeOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/owner/notice")
@RequiredArgsConstructor
@Tag(name = "Notice 사장님 API")
public class NoticeOwnerController {
    private final NoticeOwnerService noticeOwnerService;
    private final NoticeService noticeService;

    @Operation(summary = "공지 목록 조회", description = "공지 목록 전체를 조회합니다.")
    @GetMapping
    public ApiResponse<Map<String, Object>> getNoticeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @CurrentMember Member member) {
        Map<String, Object> noticePage = noticeOwnerService.getNoticeList(PageRequest.of(page, size), member.getId());
        return ApiResponse.onSuccess(noticePage);
    }

    @Operation(summary = "공지 상세 조회", description = "공지사항 하나를 상세 조회합니다.")
    @GetMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeDetailResponse>> getNoticeDetail(@PathVariable Long noticeId, @CurrentMember Member member) {
        NoticeDetailResponse noticeDetail = noticeService.getNoticeDetail(noticeId);
        return ResponseEntity.ok(ApiResponse.onSuccess(noticeDetail));
    }
}
