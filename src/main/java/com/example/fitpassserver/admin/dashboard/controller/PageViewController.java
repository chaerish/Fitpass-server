package com.example.fitpassserver.admin.dashboard.controller;

import com.example.fitpassserver.admin.dashboard.converter.PageViewConverter;
import com.example.fitpassserver.admin.dashboard.dto.response.PageViewResponseDTO;
import com.example.fitpassserver.admin.dashboard.util.VisitorUtil;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pageView")
@Tag(name = "페이지 뷰 API")
public class PageViewController {

    private final VisitorUtil visitorUtil;

    @PostMapping
    @Operation(summary = "페이지 뷰 세는 API", description = "페이지를 라우팅할 때마다 보내주시면 되며 응답은 현재 페이지 뷰 수인데 무시하셔도 좋습니다.")
    public ApiResponse<PageViewResponseDTO.IncreasePageViewResponseDTO> increasePageView() {
        return ApiResponse.onSuccess(PageViewConverter.toIncreasePageViewResponseDTO(visitorUtil.increasePageView()));
    }

}
