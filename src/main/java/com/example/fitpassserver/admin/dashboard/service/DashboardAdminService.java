package com.example.fitpassserver.admin.dashboard.service;

import com.example.fitpassserver.admin.dashboard.entity.DashBoard;
import org.springframework.data.domain.Page;

public interface DashboardAdminService {
    DashBoard createDashBoardData();
    Page<DashBoard> getDashboards(int page, int size);
}
