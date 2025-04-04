package com.example.fitpassserver.admin.fitness.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FitnessAdminRequestDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FitnessReqDTO {
        public String fitnessName;
        public String address;
        public String detailAddress;
        public String phoneNumber;
        public Integer fee;
        public Integer totalFee;
        public List<String> categoryList = new ArrayList<>();
        public boolean isPurchasable;
        private String notice;
        private Map<String, String > time;
        private String howToUse;
        private Double latitude;
        private Double longitude;
    }
}
