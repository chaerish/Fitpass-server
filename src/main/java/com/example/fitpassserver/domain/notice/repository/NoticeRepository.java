package com.example.fitpassserver.domain.notice.repository;

import com.example.fitpassserver.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findNoticeById(Long noticeId);
    List<Notice> findByIsHomeSlideTrueAndIsDraftFalse();
    Page<Notice> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Notice> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword, Pageable pageable);
    long countByIsHomeSlideTrue();
    List<Notice> findByIsDraftTrueOrderByCreatedAtDesc();

    @Query("""
        SELECT n FROM Notice n
        WHERE n.isDraft = false
        ORDER BY n.createdAt DESC
    """)
    Page<Notice> findPublishedNoticesSorted(Pageable pageable);


    @Query("SELECT n FROM Notice n WHERE n.isDraft = false AND n.isMemberSlide = true ORDER BY n.createdAt DESC")
    Page<Notice> findPublishedNoticesByIsMemberSlideTrue(Pageable pageable);

    @Query("SELECT n FROM Notice n WHERE n.isDraft = false AND n.isMemberSlide = true AND n.isHomeSlide = true")
    List<Notice> findNoticeHomeSlideIsMemberSlideTrue();
}