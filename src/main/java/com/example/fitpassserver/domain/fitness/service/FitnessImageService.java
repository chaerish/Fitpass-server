package com.example.fitpassserver.domain.fitness.service;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FitnessImageService {
    private final S3Service s3Service;
    private final FitnessRepository fitnessRepository;

    /* 피트니스 이미지 조회 */
    public String getFitnessImage(Long fitnessId) {
        Fitness fitness = fitnessRepository.findById(fitnessId)
                .orElseThrow(() -> new FitnessException(FitnessErrorCode.FITNESS_IMAGE_NOT_FOUND));

        if (fitness.getFitnessImage() != null && !fitness.getFitnessImage().equals("none")) {
            return s3Service.getGetS3Url(fitnessId, fitness.getFitnessImage()).getPreSignedUrl();
        } else {
            return "none";
        }
    }

    /* 피트니스 추가 이미지 조회 (여러 개) */
    public List<String> getAdditionalImages(Long fitnessId) {
        Fitness fitness = fitnessRepository.findById(fitnessId)
                .orElseThrow(() -> new FitnessException(FitnessErrorCode.FITNESS_IMAGE_NOT_FOUND));

        return fitness.getAdditionalImages().stream()
                .map(image -> s3Service.getGetS3Url(fitnessId, image.getImageKey()).getPreSignedUrl())
                .collect(Collectors.toList()); // 🔹 모든 추가 이미지 URL을 리스트로 반환
    }
}