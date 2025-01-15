package com.example.fitpassserver.domain.fitness.service;

import com.example.fitpassserver.domain.fitness.controller.response.FitnessRecommendResponse;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.domain.fitness.util.DistanceCalculator;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FitnessRecommendService {
    private final FitnessRepository fitnessRepository;

    public FitnessRecommendService(FitnessRepository fitnessRepository) {
        this.fitnessRepository = fitnessRepository;
    }

    public List<Fitness> getRecommendFitness() {
        return fitnessRepository.findByIsRecommendTrue();
    }

    public List<FitnessRecommendResponse> getRecommendFitnessAsResponse(double userLatitude, double userLongitude) {
        List<Fitness> fitnessList = getRecommendFitness();
        return fitnessList.stream()
                .map(fitness -> {
                    double distance = DistanceCalculator.distance(
                            userLatitude, userLongitude,
                            fitness.getLatitude(), fitness.getLongitude()
                    );

                    return FitnessRecommendResponse.builder()
                            .fitnessId(fitness.getId())
                            .name(fitness.getName())
                            .distance(distance)
                            .build();
                })
                .collect(Collectors.toList());
    }
}