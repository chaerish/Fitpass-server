package com.example.fitpassserver.admin.dashboard.dto.response;

import lombok.*;

public class PageViewResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class IncreasePageViewResponseDTO {
        private int pageView;
    }
}
