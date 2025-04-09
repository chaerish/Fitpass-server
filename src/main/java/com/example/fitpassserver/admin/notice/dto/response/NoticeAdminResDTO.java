package com.example.fitpassserver.admin.notice.dto.response;

public record NoticeAdminResDTO (
        Long id,
        String imageUrl,
        String title,
        String category,
        java.time.LocalDateTime createdAt,
        String status,
        boolean isHomeSlide,
        boolean isMemberSlide
){
}