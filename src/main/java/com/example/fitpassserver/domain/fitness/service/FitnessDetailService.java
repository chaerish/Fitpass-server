package com.example.fitpassserver.domain.fitness.Service;

import com.example.fitpassserver.domain.fitness.Controller.response.FitnessDetailResponse;
import com.example.fitpassserver.domain.fitness.Repository.FitnessRepository;
import com.example.fitpassserver.domain.fitness.Util.DistanceCalculator;
import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class FitnessDetailService {
    private final FitnessRepository fitnessRepository;

    public FitnessDetailService(FitnessRepository fitnessRepository) {
        this.fitnessRepository = fitnessRepository;
    }
    public FitnessDetailResponse getFitnessDetail(Long fitnessId, double userLatitude, double userLongitude) {
        Fitness fitness = fitnessRepository.findById(fitnessId)
                .orElseThrow(() -> new IllegalArgumentException("Fitness not found with id: " + fitnessId));

        double distance = DistanceCalculator.distance(
                userLatitude, userLongitude,
                fitness.getLatitude(), fitness.getLongitude()
        );
        String categories = fitness.getCategoryList().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.joining(", "));

        return FitnessDetailResponse.builder()
                .fitnessId(fitness.getId())
                .fitnessName(fitness.getName())
                .address(fitness.getAddress())
                .phoneNumber(fitness.getPhoneNumber())
                .categoryName(categories)
                .notice(fitness.getNotice())
                .time(fitness.getTime())
                .howToUse(fitness.getHowToUse())
                .etc(fitness.getEtc())
                .fee(fitness.getFee())
                .distance(distance)
                .build();
    }
}