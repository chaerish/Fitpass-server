package com.example.fitpassserver.domain.plan.dto.response;

import java.time.LocalDateTime;

public record SIDCheckResponseDTO(
        String available,
        String cid,
        String sid,
        String status,
        String item_name,
        String payment_method_type,
        LocalDateTime created_at,
        LocalDateTime last_approved_at,
        LocalDateTime use_point_status
) {
}
