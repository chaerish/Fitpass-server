package com.example.fitpassserver.domain.fitness.service;

import com.example.fitpassserver.domain.fitness.controller.response.FitnessSearchResponse;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.domain.fitness.util.DistanceCalculator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FitnessSearchService {
    private final FitnessRepository fitnessRepository;

    public FitnessSearchService(FitnessRepository fitnessRepository) {
        this.fitnessRepository = fitnessRepository;
    }

    public List<FitnessSearchResponse> searchFitnessByKeyword(String keyword, double userLatitude, double userLongitude) {
        List<Fitness> fitnessList = fitnessRepository.findByNameContaining(keyword);

        return fitnessList.stream()
                .map(fitness -> {
                    double distance = DistanceCalculator.distance(
                            userLatitude, userLongitude,
                            fitness.getLatitude(), fitness.getLongitude()
                    );

                    return FitnessSearchResponse.builder()
                            .fitnessId(fitness.getId())
                            .fitnessName(fitness.getName())
                            .address(fitness.getAddress())
                            .fee(fitness.getFee())
                            .distance(distance) // 거리 추가
                            .build();
                })
                .collect(Collectors.toList());
    }
}