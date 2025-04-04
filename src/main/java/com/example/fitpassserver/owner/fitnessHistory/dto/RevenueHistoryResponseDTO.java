package com.example.fitpassserver.owner.fitnessHistory.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RevenueHistoryResponseDTO(
        List<RevenueHistoryDetailDTO> revenueHistoryDetailDTOS,
        long totalElements,
        int totalPage

) {
    public record RevenueHistoryDetailDTO(
            String description,
            LocalDateTime time,
            Long totalPrice
    ) {
    }
}
