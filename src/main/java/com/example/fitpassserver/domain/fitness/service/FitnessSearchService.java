package com.example.fitpassserver.domain.fitness.service;

import com.example.fitpassserver.domain.fitness.controller.response.CursorPaginationResponse;
import com.example.fitpassserver.domain.fitness.controller.response.FitnessSearchResponse;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.domain.fitness.util.DistanceCalculator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FitnessSearchService {
    private final FitnessRepository fitnessRepository;

    public FitnessSearchService(FitnessRepository fitnessRepository) {
        this.fitnessRepository = fitnessRepository;
    }

    public CursorPaginationResponse<FitnessSearchResponse> searchFitnessByKeyword(
            String keyword, Long cursor, int size, double userLatitude, double userLongitude
    ) {
        keyword = keyword.trim();
        Pageable pageable = PageRequest.of(0, size);

        List<Fitness> fitnessList = (cursor == null)
                ? fitnessRepository.findTopByNameContaining(keyword, pageable) // 처음 요청 시
                : fitnessRepository.findNextByNameContaining(keyword, cursor, pageable); // 이후 요청 시

        List<FitnessSearchResponse> responses = fitnessList.stream()
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
                            .distance(distance)
                            .build();
                })
                .sorted((r1, r2) -> Double.compare(r1.getDistance(), r2.getDistance()))
                .collect(Collectors.toList());
        Long nextCursor = fitnessList.isEmpty() ? null : fitnessList.get(fitnessList.size() - 1).getId();
        return new CursorPaginationResponse<>(responses, nextCursor);
    }
}
