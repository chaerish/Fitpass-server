package com.example.fitpassserver.admin.notice.dto.response;
import java.time.LocalDate;

public record NoticeAdminResDTO (
        Long id,
        String imageUrl,
        String title,
        String category,
        LocalDate createdAt,
        String status,
        boolean isHomeSlide
){
}