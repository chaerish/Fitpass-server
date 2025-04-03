package com.example.fitpassserver.owner.dashboard.controller;

import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.owner.dashboard.service.DashboardOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard 사장님 API")
public class DashboardOwnerController {

    private final DashboardOwnerService dashboardOwnerService;

    @Operation(summary = "공지사항 3개 조회 API", description = "공지사항 3개 조회합니다.")
    @GetMapping
    public ApiResponse<?> getNotices(@CurrentMember Member member) {
        Map<String, Object> notices = dashboardOwnerService.getNotices();
        return ApiResponse.onSuccess(notices);
    }
}
