package com.example.fitpassserver.domain.fitness.Controller.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
@AllArgsConstructor
public class FitnessSearchResponse {
    private Long fitnessId;
    private String fitnessName;
    private String address;
    private Integer fee;
    private double distance;
}