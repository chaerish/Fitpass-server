package com.example.fitpassserver.domain.notice.repository;

import com.example.fitpassserver.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findNoticeById(Long noticeId);
    List<Notice> findByIsHomeSlideTrueAndIsDraftFalse();
    Page<Notice> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Notice> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword, Pageable pageable);
    long countByIsHomeSlideTrue();
}