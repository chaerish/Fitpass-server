package com.example.fitpassserver.admin.owner.controller;

import com.example.fitpassserver.admin.owner.dto.OwnerAdminResponseDTO;
import com.example.fitpassserver.admin.owner.service.query.OwnerAdminQueryService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "어드민 시설회원 정보 API", description = "어드민 시설회원 정보 API입니다.")
public class OwnerAdminController {

    private final OwnerAdminQueryService ownerAdminQueryService;

    @Operation(summary = "어드민 시설회원 정보 조회", description = "어드민이 시설회원의 정보를 조회하는 API 입니다.")
    @GetMapping("/owners")
    public ApiResponse<?> getOwnersPage(
            @Parameter(description = "현재 인증된 사용자 정보", hidden = true) @CurrentMember Member member,
            @Parameter(description = "페이지 시작 오프셋 (기본값: 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 당 엘리먼트 개수 (기본값: 10)", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "검색 유형 (예: name, loginId, phoneNumber)", example = "name") @RequestParam(required = false) String searchType,
            @Parameter(description = "검색 키워드", example = "핏패스") @RequestParam(required = false) String keyword) {
        OwnerAdminResponseDTO.OwnerPagesDTO ownersInfo = ownerAdminQueryService.getOwnersInfo(page, size, searchType, keyword);
        return ApiResponse.onSuccess(ownersInfo);
    }

}
