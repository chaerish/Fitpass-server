package com.example.fitpassserver.owner.fitnessHistory.dto;

import java.time.LocalDateTime;
import java.util.List;


public record UsageHistoryResponseDTO(
        List<FitnessUsageDetailDTO> fitnessUsageDetailDTOS,
        long totalElements,
        int totalPages
) {
    public record FitnessUsageDetailDTO(
            String loginId,
            String memberName,
            LocalDateTime activeTime

    ) {
    }
}
