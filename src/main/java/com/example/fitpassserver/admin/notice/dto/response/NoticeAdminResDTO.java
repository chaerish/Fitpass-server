package com.example.fitpassserver.admin.notice.dto.response;

import java.time.LocalDateTime;

public record NoticeAdminResDTO (
        Long id,
        String imageUrl,
        String title,
        String category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String status,
        boolean isMemberHomeSlide,
        boolean isOwnerHomeSlide,
        boolean isMemberSlide,
        boolean isOwnerSlide
){
}