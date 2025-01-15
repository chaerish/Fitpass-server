package com.example.fitpassserver.domain.fitness.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FitnessDetailResponse {
    private Long fitnessId;
    private String fitnessName;
    private String address;
    private String phoneNumber;
    private String categoryName;
    private String notice;
    private String time;
    private String howToUse;
    private String etc;
    private Integer fee;
    private Double distance;
}