package com.example.fitpassserver.domain.notice.entity;

import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "notice")
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "views")
    @Builder.Default
    private Long views = 0L;

    @Column(name = "notice_image")
    private String noticeImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeType type;

    @Column(nullable = false)
    private boolean isHomeSlide; // true: 홈 슬라이드 게시, false: 미게시

    @Column(nullable = false)
    private boolean isDraft;  // true: 임시저장, false: 정식 등록

    // 🔹 조회수 증가 메서드 추가
    public void increaseViews() {
        this.views += 1;
    }
    public void setHomeSlide(boolean isHomeSlide) {
        this.isHomeSlide = isHomeSlide;
    }
}
