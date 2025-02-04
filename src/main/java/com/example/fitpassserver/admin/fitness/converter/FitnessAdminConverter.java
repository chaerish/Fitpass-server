package com.example.fitpassserver.admin.fitness.converter;

import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.domain.fitness.entity.Fitness;

import java.util.List;

public class FitnessAdminConverter {

    public static Fitness toEntity(FitnessAdminRequestDTO.CreateFitnessDTO dto, String mainImageKey, List<String> additionalImages){
        return Fitness.builder()
                .name(dto.getFitnessName())
                .fitnessImage(mainImageKey)
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .fee(dto.getFee())
                .totalFee(dto.getTotalFee())
                .isPurchasable(dto.isPurchasable())
                .notice(dto.getNotice())
                .time(dto.getTime())
                .howToUse(dto.getHowToUse())
                .etc(dto.getEtc())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .distance(0d)
                .isRecommend(false)
                .build();
    }
}
