package com.example.fitpassserver.domain.notice.controller.response;

import java.time.LocalDateTime;

public record NoticeDetailResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        String imageUrl
) {
}