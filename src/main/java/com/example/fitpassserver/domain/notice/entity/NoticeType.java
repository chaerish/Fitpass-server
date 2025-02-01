package com.example.fitpassserver.domain.notice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeType {
    ANNOUNCEMENT("공지사항"),
    EVENT("이벤트");

    private final String value;
}