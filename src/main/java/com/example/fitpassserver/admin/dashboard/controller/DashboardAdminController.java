package com.example.fitpassserver.admin.dashboard.controller;

import com.example.fitpassserver.admin.dashboard.converter.DashBoardConverter;
import com.example.fitpassserver.admin.dashboard.dto.response.DashBoardResponseDTO;
import com.example.fitpassserver.admin.dashboard.service.DashboardAdminService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
@Tag(name = "대시보드 API")
public class DashboardAdminController {

    private final DashboardAdminService dashboardAdminService;

    @Operation(summary = "대시보드 API", description = "대시보드 데이터 API")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 첫 페이지: 1"),
            @Parameter(name = "size", description = "페이지 크기, 기본값: 10")
    })
    @GetMapping
    public ApiResponse<DashBoardResponseDTO.DashBoardInfoListDTO> getDashboards(@RequestParam(required = false, defaultValue = "1") int page,
                                                                                @RequestParam(required = false, defaultValue = "10") int size) {
        DashBoardResponseDTO.DashBoardInfoListDTO response = DashBoardConverter.toDashBoardInfoListDTO(
                dashboardAdminService.getDashboards(page, size)
        );
        return ApiResponse.onSuccess(response);
    }
}
