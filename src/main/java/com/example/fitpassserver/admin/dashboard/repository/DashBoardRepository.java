package com.example.fitpassserver.admin.dashboard.repository;

import com.example.fitpassserver.admin.dashboard.entity.DashBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashBoardRepository extends JpaRepository<DashBoard, Long> {
}
