package com.example.fitpassserver.admin.dashboard.converter;

import com.example.fitpassserver.admin.dashboard.dto.response.PageViewResponseDTO;

public class PageViewConverter {

    public static PageViewResponseDTO.IncreasePageViewResponseDTO toIncreasePageViewResponseDTO(int pageView) {
        return PageViewResponseDTO.IncreasePageViewResponseDTO.builder()
                .pageView(pageView)
                .build();
    }
}
