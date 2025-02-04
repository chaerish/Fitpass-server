package com.example.fitpassserver.admin.notice.service;

import org.springframework.data.domain.Pageable;
import java.util.Map;

public interface NoticeAdminServiceImpl {
    Map<String, Object> getNoticeAdminList(String keyword, Pageable pageable);
    void updateHomeSlideStatus(Long noticeId, boolean isHomeSlide);
}
