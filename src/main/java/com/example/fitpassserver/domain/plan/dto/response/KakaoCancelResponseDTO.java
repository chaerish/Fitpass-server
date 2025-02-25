package com.example.fitpassserver.domain.plan.dto.response;

import java.time.LocalDateTime;

public record KakaoCancelResponseDTO(
        String cid,                       // 가맹점 코드, 10자
        String sid,                       // 정기 결제 고유 번호, 20자
        String status,                    // 정기 결제 상태, ACTIVE 또는 INACTIVE 중 하나
        LocalDateTime created_at,         // SID 발급 시각
        LocalDateTime last_approved_at,   // 마지막 결제 승인 시각
        LocalDateTime inactivated_at    // 정기 결제 비활성화 시각
) {
}