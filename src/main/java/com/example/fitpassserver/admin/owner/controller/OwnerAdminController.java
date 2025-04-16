package com.example.fitpassserver.admin.owner.controller;

import com.example.fitpassserver.admin.owner.dto.OwnerAdminResponseDTO;
import com.example.fitpassserver.admin.owner.service.command.OwnerAdminCommandService;
import com.example.fitpassserver.admin.owner.service.query.OwnerAdminQueryService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.owner.owner.entity.OwnerStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/owner")
@Tag(name = "어드민 시설회원 API", description = "어드민 시설회원 API입니다.")
public class OwnerAdminController {

    private final OwnerAdminQueryService ownerAdminQueryService;
    private final OwnerAdminCommandService ownerAdminCommandService;

    @Operation(summary = "어드민 시설회원 정보 조회", description = "어드민이 시설회원의 정보를 조회하는 API 입니다.")
    @GetMapping("/info")
    public ApiResponse<?> getOwnersPage(
            @Parameter(description = "현재 인증된 사용자 정보", hidden = true) @CurrentMember Member member,
            @Parameter(description = "페이지 시작 오프셋 (기본값: 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 당 엘리먼트 개수 (기본값: 10)", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "검색 유형 (예: corporation,phoneNumber,status)", example = "status") @RequestParam(required = false) String searchType,
            @Parameter(description = "검색 키워드", example = "승인대기") @RequestParam(required = false) String keyword) {
        OwnerAdminResponseDTO.OwnerPagesDTO ownersInfo = ownerAdminQueryService.getOwnersInfo(page, size, searchType, keyword);
        return ApiResponse.onSuccess(ownersInfo);
    }

    @Operation(summary = "어드민 시설회원 승인 요청 조회", description = "어드민이 시설회원 승인요청을 조회하는 API 입니다.")
    @GetMapping("/request")
    public ApiResponse<?> getApprovalOwnersPage(
            @Parameter(description = "현재 인증된 사용자 정보", hidden = true) @CurrentMember Member member,
            @Parameter(description = "페이지 시작 오프셋 (기본값: 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 당 엘리먼트 개수 (기본값: 10)", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "검색 유형 (예: corporation,phoneNumber)", example = "corporation") @RequestParam(required = false) String searchType,
            @Parameter(description = "검색 키워드", example = "핏패스") @RequestParam(required = false) String keyword) {
        OwnerAdminResponseDTO.OwnerApprovalPagesDTO ownersInfo = ownerAdminQueryService.getOwnersApproval(page, size, searchType, keyword);
        return ApiResponse.onSuccess(ownersInfo);
    }

    @Operation(summary = "시설회원 반려", description = "어드민이 시설회원을 반려하는 API입니다.")
    @PatchMapping("/refusal")
    public ApiResponse<?> patchRefusalOwner(
            @Parameter(description = "반려할 시설회원의 loginId") @RequestParam String loginId) {
        OwnerStatus status = OwnerStatus.STOPPED;
        ownerAdminCommandService.patchOwnerStatus(loginId, status);
        return ApiResponse.onSuccess("반려되었습니다.");
    }

    @Operation(summary = "시설회원 승인", description = "어드민이 시설회원을 승인하는 API입니다.")
    @PatchMapping("/approval")
    public ApiResponse<?> patchApprovalOwner(
            @Parameter(description = "승인할 시설회원의 loginId") @RequestParam String loginId) {
        OwnerStatus status = OwnerStatus.UNREGISTERED;
        ownerAdminCommandService.patchOwnerStatus(loginId, status);
        return ApiResponse.onSuccess("승인되었습니다.");
    }


}
