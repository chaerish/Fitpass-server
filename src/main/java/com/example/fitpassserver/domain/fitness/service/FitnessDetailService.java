package com.example.fitpassserver.domain.fitness.service;

import com.example.fitpassserver.domain.fitness.controller.response.FitnessDetailResponse;
import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.domain.fitness.util.DistanceCalculator;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class FitnessDetailService {
    private final FitnessRepository fitnessRepository;
    private final FitnessImageService fitnessImageService;

    public FitnessDetailService(FitnessRepository fitnessRepository, S3Service s3Service, FitnessImageService fitnessImageService) {
        this.fitnessRepository = fitnessRepository;
        this.fitnessImageService = fitnessImageService;
    }
    public FitnessDetailResponse getFitnessDetail(Long fitnessId, double userLatitude, double userLongitude) {
        Fitness fitness = fitnessRepository.findById(fitnessId)
                .orElseThrow(() -> new FitnessException(FitnessErrorCode.FITNESS_NOT_FOUND));

        double distance = DistanceCalculator.distance(
                userLatitude, userLongitude,
                fitness.getLatitude(), fitness.getLongitude()
        );
        String imageUrl = fitnessImageService.getFitnessImage(fitnessId);

        String additionalImageUrl = fitnessImageService.getAdditionalImage(fitnessId);

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
                .imageUrl(imageUrl)
                .fitnessLatitude(fitness.getLatitude())
                .fitnessLongitude(fitness.getLongitude())
                .additionalImage(additionalImageUrl)
                .build();
    }
}