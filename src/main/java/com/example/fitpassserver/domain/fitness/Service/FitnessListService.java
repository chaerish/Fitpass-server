package com.example.fitpassserver.domain.fitness.Service;

import com.example.fitpassserver.domain.fitness.Controller.response.FitnessListResponse;
import com.example.fitpassserver.domain.fitness.Repository.FitnessRepository;
import com.example.fitpassserver.domain.fitness.Util.DistanceCalculator;
import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FitnessListService {
    private final FitnessRepository fitnessRepository;

    public FitnessListService(FitnessRepository fitnessRepository) {
        this.fitnessRepository = fitnessRepository;
    }

    public List<FitnessListResponse> getFitnessList(String categoryName, String sort, double userLatitude, double userLongitude, int page, int size) {
        Page<Fitness> fitnessPage = fitnessRepository.findByCategoryList_CategoryName(categoryName, PageRequest.of(page, size));

        return fitnessPage.stream()
                .map(fitness -> {
                    double distance = DistanceCalculator.distance(
                            userLatitude, userLongitude,
                            fitness.getLatitude(), fitness.getLongitude()
                    );
                    String categories = fitness.getCategoryList().stream()
                            .map(Category::getCategoryName)
                            .collect(Collectors.joining(", "));

                    return FitnessListResponse.builder()
                            .fitnessId(fitness.getId())
                            .fitnessName(fitness.getName())
                            .address(fitness.getAddress())
                            .distance(distance)
                            .fee(fitness.getFee())
                            .categoryName(categories)
                            .build();
                })
                .sorted((f1, f2) -> switch (sort) {
                    case "distance" -> Double.compare(f1.getDistance(), f2.getDistance());
                    case "lowPrice" -> Integer.compare(f1.getFee(), f2.getFee());
                    case "highPrice" -> Integer.compare(f2.getFee(), f1.getFee());
                    default -> Double.compare(f1.getDistance(), f2.getDistance()); // 기본값: 거리순 정렬
                })
                .collect(Collectors.toList());
    }
}