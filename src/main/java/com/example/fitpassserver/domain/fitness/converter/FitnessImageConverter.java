package com.example.fitpassserver.domain.fitness.converter;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.FitnessImage;

import java.util.List;
import java.util.stream.Collectors;

public class FitnessImageConverter {

    public static List<FitnessImage> saveFitnessImage(List<String> additionalImageUrls, Fitness fitness){
        return additionalImageUrls.stream()
                .map(key -> FitnessImage.builder()
                        .fitness(fitness)
                        .imageKey(key)
                        .build())
                .collect(Collectors.toList());
    }
}
