package com.example.fitpassserver.admin.notice.dto.request;

import com.example.fitpassserver.admin.notice.exception.NoticeAdminErrorCode;
import com.example.fitpassserver.domain.notice.entity.NoticeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Schema(description = "임시저장 여부 (true: 임시저장, false: 정식 등록)", example = "false")
    private boolean isDraft;

    // ✅ 유효성 검사 (정식 등록 시 필수값 체크)
    @AssertTrue(message = "NOTICE401")
    private boolean isTitleValid() {
        if (isDraft) return true;  // ✅ 임시 저장일 경우 무조건 통과
        return title != null && !title.isBlank();
    }

    @AssertTrue(message = "NOTICE402")
    private boolean isContentValid() {
        if (isDraft) return true;  // ✅ 임시 저장일 경우 무조건 통과
        return content != null && !content.isBlank();
    }

    @AssertTrue(message = "NOTICE403")
    private boolean isTypeValid() {
        if (isDraft) return true;  // ✅ 임시 저장일 경우 무조건 통과
        return type != null;
    }
}