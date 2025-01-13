package com.example.fitpassserver.domain.fitness.Controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FitnessRecommendResponse {
    private Long fitnessId;
    private String name;
    private double distance;
}