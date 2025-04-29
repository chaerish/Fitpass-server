package com.example.fitpassserver.admin.fitness.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FitnessAdminPreviewDTO {
        public Long id;
        public String loginId;
        public String fitnessName;
        public String address;
        public String detailAddress;
        public String phoneNumber;
        public Integer fee;
        public Integer totalFee;
        public List<String> categoryList;
        public boolean isPurchasable;
        private String notice;
        private Map<String, String > time;
        private String howToUse;
        private Double latitude;
        private Double longitude;
        private String fitnessImage;
        private List<String> additionalImages;
    }
}

