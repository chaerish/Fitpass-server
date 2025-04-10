package com.example.fitpassserver.domain.fitness.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FitnessDetailResponse {
    private Long fitnessId;
    private String fitnessName;
    private String address;
    private String detailAddress;
    private String phoneNumber;
    private String categoryName;
    private String notice;
    private String time;
    private String howToUse;
    private Integer fee;
    private Double distance;
    private String imageUrl;
    private Double fitnessLatitude;
    private Double fitnessLongitude;
    private List<String> additionalImages;
}