package com.example.fitpassserver.admin.fitness.dto.request;

import com.example.fitpassserver.domain.fitness.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FitnessAdminRequestDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateFitnessDTO {
        public String fitnessName;
        public String address;
        public String phoneNumber;
        public Integer fee;
        public Integer totalFee;
        public List<String> categoryList = new ArrayList<>();
        public boolean isPurchasable;
        private String notice;
        private String time;
        private String howToUse;
        private String etc;
        private Double latitude;
        private Double longitude;

    }
}
