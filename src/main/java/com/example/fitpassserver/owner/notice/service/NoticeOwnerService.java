package com.example.fitpassserver.owner.notice.service;

import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface NoticeOwnerService {
    Map<String, Object> getNoticeList(Pageable pageable, Long memberId);
}
