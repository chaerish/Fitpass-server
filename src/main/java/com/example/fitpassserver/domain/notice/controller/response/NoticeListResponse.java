package com.example.fitpassserver.domain.notice.controller.response;

import com.example.fitpassserver.domain.notice.entity.NoticeType;

public record NoticeListResponse(
        Long id,
        String title,
        String noticeType
) {
}