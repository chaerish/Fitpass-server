package com.example.fitpassserver.admin.fitness.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class FitnessAdminResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FitnessInfoDTO{
        private Long fitnessId;
        private String fitnessName;
        private List<String> categoryNames;
        private Integer totalFee;
        private String phoneNumber;
        private LocalDateTime createdAt;
        private boolean isPurchasable;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FitnessListDTO{
        private List<FitnessInfoDTO> fitnessList;
        private long totalElements;
        private int totalPages;
    }

}
