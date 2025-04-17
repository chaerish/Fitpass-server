package com.example.fitpassserver.admin.notice.dto.request;

import com.example.fitpassserver.domain.notice.entity.NoticeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeAdminReqDTO {

    @Schema(description = "공지사항 ID (새 글 작성 시 입력 안하셔도 됩니다 null, 수정 시 필수)", example = "1, 새로 등록할 경우엔 null")
    private Long id;

    @Schema(description = "공지 제목 (정식 등록 시 필수)", example = "제목 1")
    private String title = "";

    @Schema(description = "공지 내용 (정식 등록 시 필수)", example = "내용")
    private String content= "";

    @Schema(description = "공지사항 유형 (정식 등록 시 필수)", example = "ANNOUNCEMENT / EVENT")
    private NoticeType type;

    @Schema(description = "회원 슬라이드 게시 여부", example = "true")
    private boolean isMemberSlide;

    @Schema(description = "사업자 슬라이드 게시 여부", example = "false")
    private boolean isOwnerSlide;

}