package com.example.fitpassserver.admin.fitness.converter;

import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.admin.fitness.dto.response.FitnessAdminResponseDTO;
import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FitnessAdminConverter {

    // Map을 형식화된 String으로 변환하는 메소드
    private static String convertMapToFormattedString(Map<String, String> timeMap) {
        if (timeMap == null) return null;

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        // 요일 순서대로 정렬하기 위한 리스트
        List<String> days = Arrays.asList("월", "화", "수", "목", "금", "토", "일");

        for (String day : days) {
            if (timeMap.containsKey(day)) {
                if (!isFirst) {
                    sb.append("\n");  // 개행 문자 추가
                }
                sb.append(day).append(" ").append(timeMap.get(day));  // 요일 + 공백 + 시간
                isFirst = false;
            }
        }

        return sb.toString();
    }

    public static Fitness toEntity(FitnessAdminRequestDTO.CreateFitnessDTO dto){
        if(dto.getTotalFee() > dto.getFee()){
            throw new FitnessException(FitnessErrorCode.INVALID_SALE_PRICE);
        }
        return Fitness.builder()
                .name(dto.getFitnessName())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .fee(dto.getFee())
                .totalFee(dto.getTotalFee())
                .isPurchasable(dto.isPurchasable())
                .notice(dto.getNotice())
                .time(convertMapToFormattedString(dto.getTime()))
                .howToUse(dto.getHowToUse())
                .etc(dto.getEtc())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .discount(dto.getFee() - dto.getTotalFee())
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
