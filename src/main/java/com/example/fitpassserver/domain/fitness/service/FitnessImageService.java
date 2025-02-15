package com.example.fitpassserver.domain.fitness.service;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    /* 피트니스 추가 이미지 조회 */
    public String getAdditionalImage(Long fitnessId) {
        Fitness fitness = fitnessRepository.findById(fitnessId)
                .orElseThrow(() -> new FitnessException(FitnessErrorCode.FITNESS_IMAGE_NOT_FOUND));

        return fitness.getAdditionalImages().isEmpty()
                ? null // 🔹 추가 이미지가 없으면 null 반환
                : s3Service.getGetS3Url(fitnessId, fitness.getAdditionalImages().get(0).getImageKey()).getPreSignedUrl();
    }
}