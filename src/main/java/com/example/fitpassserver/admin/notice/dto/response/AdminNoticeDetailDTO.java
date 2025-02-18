package com.example.fitpassserver.admin.notice.dto.response;

public record AdminNoticeDetailDTO(
        Long id,
        String title,
        String content,
        String imageUrl,
        String category
) {
}
