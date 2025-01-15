package com.example.fitpassserver.domain.notice.repository;

import com.example.fitpassserver.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}