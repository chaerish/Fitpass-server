package com.example.fitpassserver.domain.fitness.Controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FitnessListResponse {
    private Long fitnessId;
    private String fitnessName;
    private String address;
    private Double distance; // 거리
    private Integer fee;
    private String categoryName; // 카테고리 이름
}