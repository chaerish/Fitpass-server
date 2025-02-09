package com.example.fitpassserver.admin.fitness.converter;

import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.admin.fitness.dto.response.FitnessAdminResponseDTO;
import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class FitnessAdminConverter {

    public static Fitness toEntity(FitnessAdminRequestDTO.CreateFitnessDTO dto){
        return Fitness.builder()
                .name(dto.getFitnessName())
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

    public static FitnessAdminResponseDTO.FitnessInfoDTO from(Fitness fitness) {

        List<String> categoryNames = fitness.getCategoryList().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());

        return FitnessAdminResponseDTO.FitnessInfoDTO.builder()
                .fitnessId(fitness.getId())
                .fitnessName(fitness.getName())
                .categoryNames(categoryNames)
                .totalFee(fitness.getTotalFee())
                .phoneNumber(fitness.getPhoneNumber())
                .createdAt(fitness.getCreatedAt())
                .isPurchasable(fitness.getIsPurchasable())
                .build();
    }

    public static FitnessAdminResponseDTO.FitnessListDTO from(Page<Fitness> page){
        return FitnessAdminResponseDTO.FitnessListDTO.builder()
                .fitnessList(page.getContent().stream()
                        .map(FitnessAdminConverter::from)
                        .collect(Collectors.toList()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
