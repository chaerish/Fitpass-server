package com.example.fitpassserver.domain.fitness.Controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FitnessListResponse {
    private Long fitnessId;
    private String fitnessName;
    private String address;
    private Double distance; // 거리
    private Integer fee;
    private String categoryName; // 카테고리 이름
}