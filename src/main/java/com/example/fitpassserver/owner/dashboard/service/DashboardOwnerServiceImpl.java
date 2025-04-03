package com.example.fitpassserver.owner.dashboard.service;

import com.example.fitpassserver.admin.notice.service.NoticeAdminService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardOwnerServiceImpl implements DashboardOwnerService {

    private final NoticeAdminService noticeAdminService;

    public Map<String, Object> getNotices() {
        Pageable pageable = PageRequest.of(0, 3);
        return noticeAdminService.getNoticeAdminList("", pageable);
    }
}
