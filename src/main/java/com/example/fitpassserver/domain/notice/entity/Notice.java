package com.example.fitpassserver.domain.notice.entity;

import com.example.fitpassserver.admin.notice.dto.request.NoticeUpdateReqDTO;
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

    @Setter
    @Column(name = "notice_image")
    private String noticeImage;

    @Enumerated(EnumType.STRING)
    @Column
    private NoticeType type;

    @Column(nullable = false)
    private boolean isMemberHomeSlide; // true: 홈 슬라이드 게시, false: 미게시

    @Column(nullable = false)
    private boolean isOwnerHomeSlide; // true: 홈 슬라이드 게시, false: 미게시

    @Setter
    @Column(nullable = false)
    private boolean isDraft;  // true: 임시저장, false: 정식 등록

    @Setter
    @Column(nullable = false)
    private boolean isMemberSlide;

    @Setter
    @Column(nullable = false)
    private boolean isOwnerSlide;

    // 🔹 조회수 증가 메서드 추가
    public void increaseViews() {
        this.views += 1;
    }
    public void updateMemberHomeSlide(boolean isHomeSlide) {
        this.isMemberHomeSlide = isHomeSlide;
    }
    public void updateOwnerHomeSlide(boolean isHomeSlide) {
        this.isOwnerHomeSlide = isHomeSlide;
    }

    public void update(NoticeUpdateReqDTO request, String imageUrl) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.type = request.getType();
        this.noticeImage = (imageUrl != null) ? imageUrl : this.noticeImage; // 기존 이미지 유지
        this.isMemberSlide = request.isMemberSlide();
        this.isOwnerSlide = request.isOwnerSlide();
    }

}
