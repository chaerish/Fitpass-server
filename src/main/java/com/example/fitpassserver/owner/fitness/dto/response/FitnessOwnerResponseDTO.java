package com.example.fitpassserver.owner.fitness.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FitnessOwnerResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FitnessPreviewDTO{
        public Long fitnessId;
        public String fitnessName;
        public String address;
        public String imageUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FitnessListDTO{
        public Long nextCursor;
        public Boolean hasNext;
        public List<FitnessPreviewDTO> fitnessList;
    }
}
