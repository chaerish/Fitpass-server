package com.example.fitpassserver.domain.fitness.service;

import com.example.fitpassserver.domain.fitness.controller.response.CursorPaginationResponse;
import com.example.fitpassserver.domain.fitness.controller.response.FitnessListResponse;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.domain.fitness.util.DistanceCalculator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FitnessListService {
    private final FitnessRepository fitnessRepository;

    public FitnessListService(FitnessRepository fitnessRepository) {
        this.fitnessRepository = fitnessRepository;
    }

    public CursorPaginationResponse<FitnessListResponse> getFitnessList(
            String categoryName,
            String sort,
            double userLatitude,
            double userLongitude,
            Long cursor,
            int size
    ) {
        List<Fitness> fitnessList = fetchFitnessListBySort(categoryName, sort, userLatitude, userLongitude, cursor, size);

        List<FitnessListResponse> responses = fitnessList.stream()
                .map(fitness -> {
                    double distance = DistanceCalculator.distance(
                            userLatitude, userLongitude,
                            fitness.getLatitude(), fitness.getLongitude()
                    );
                    String categories = fitness.getCategoryList().stream()
                            .map(Category::getCategoryName)
                            .collect(Collectors.joining(", "));

                    return new FitnessListResponse(
                            fitness.getId(),
                            fitness.getName(),
                            fitness.getAddress(),
                            distance,
                            fitness.getFee(),
                            categories
                    );
                })
                .collect(Collectors.toList());

        Long nextCursor = fitnessList.isEmpty() ? null : fitnessList.get(fitnessList.size() - 1).getId();
        return new CursorPaginationResponse<>(responses, nextCursor);
    }
    private List<Fitness> fetchFitnessListBySort(String categoryName, String sort, double userLatitude, double userLongitude, Long cursor, int size) {
        Sort sortingCriteria = switch (sort) {
            case "lowPrice" -> Sort.by(Sort.Direction.ASC, "fee");
            case "highPrice" -> Sort.by(Sort.Direction.DESC, "fee");
            default -> Sort.unsorted();
        };
        Pageable pageable = PageRequest.of(0, size, sortingCriteria);
        List<Fitness> fitnessList;
        if (cursor != null) {
            fitnessList = fitnessRepository.findByCategoryList_CategoryNameAndIdGreaterThan(
                    categoryName, cursor, pageable
            ).getContent();
        } else {
            fitnessList = fitnessRepository.findByCategoryList_CategoryName(
                    categoryName, pageable
            ).getContent();
        }
        if ("distance".equals(sort)) {
            fitnessList = fitnessList.stream()
                    .sorted((f1, f2) -> Double.compare(
                            DistanceCalculator.distance(userLatitude, userLongitude, f1.getLatitude(), f1.getLongitude()),
                            DistanceCalculator.distance(userLatitude, userLongitude, f2.getLatitude(), f2.getLongitude())
                    ))
                    .collect(Collectors.toList());
        }
        return fitnessList;
    }
}