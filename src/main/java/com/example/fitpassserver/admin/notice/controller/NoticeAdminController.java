package com.example.fitpassserver.admin.notice.controller;

import com.example.fitpassserver.admin.notice.dto.request.NoticeAdminReqDTO;
import com.example.fitpassserver.admin.notice.dto.response.NoticeAdminResDTO;
import com.example.fitpassserver.admin.notice.service.NoticeAdminService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Notice 어드민 API", description = "공지사항 관리자 API")
@RestController
@RequestMapping("/admin/notice")
public class NoticeAdminController {

    private final NoticeAdminService noticeAdminService;

    public NoticeAdminController(NoticeAdminService noticeAdminService) {
        this.noticeAdminService = noticeAdminService;
    }

    @Operation(summary = "어드민 공지 목록 조회(키워드 검색 포함)", description = "어드민 공지사항 목록을 조회합니다. 검색어가 없을 땐 파라미터 null")
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

    @Operation(summary = "홈 슬라이드 게시 체크박스", description = "홈 슬라이드에 게시 할건지에 대한 값을 저장 true = 게시")
    @PatchMapping("/{noticeId}/home-slide-check")
    public ResponseEntity<ApiResponse<Void>> updateHomeSlide(
            @PathVariable Long noticeId,
            @RequestParam boolean isHomeSlide
    ) {
        noticeAdminService.updateHomeSlideStatus(noticeId, isHomeSlide);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "공지사항 임시저장")
    @PostMapping(value = "/draft", consumes = {"multipart/form-data"})
    public ApiResponse<NoticeAdminResDTO> saveDraft(
            @RequestPart("request") NoticeAdminReqDTO request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        NoticeAdminResDTO noticeId = noticeAdminService.saveNotice(request, image, true);
        return ApiResponse.onSuccess(noticeId);
    }

    @Operation(summary = "공지사항 저장 (정식 등록)")
    @PostMapping(value = "/save", consumes = {"multipart/form-data"})
    public ApiResponse<NoticeAdminResDTO> saveNotice(
            @RequestPart("request") NoticeAdminReqDTO request,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        NoticeAdminResDTO response = noticeAdminService.saveNotice(request, image, false);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "임시 저장된 공지사항 목록 조회")
    @GetMapping(value = "/draftList")
    public ApiResponse<Map<String, Object>> getDraftList() {
        List<String> draftTitles = noticeAdminService.getDraftNotices();
        Map<String, Object> response = new HashMap<>();
        response.put("titles", draftTitles);
        response.put("count", draftTitles.size());
        return ApiResponse.onSuccess(response);
    }
}