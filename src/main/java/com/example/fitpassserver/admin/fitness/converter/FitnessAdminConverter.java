package com.example.fitpassserver.admin.fitness.converter;

import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.admin.fitness.dto.response.FitnessAdminResponseDTO;
import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;

import java.util.*;
import java.util.stream.Collectors;

public class FitnessAdminConverter {

    // Map을 형식화된 String으로 변환하는 메소드
    public static String convertMapToFormattedString(Map<String, String> timeMap) {
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

    public static Map<String, String> convertFormattedStringToMap(String formattedString) {
        if (formattedString == null || formattedString.trim().isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, String> timeMap = new HashMap<>();
        String[] lines = formattedString.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                // 첫 번째 글자는 요일로, 나머지는 시간으로 파싱
                if (line.length() >= 2) {
                    String day = line.substring(0, 1);  // 첫 글자를 요일로
                    String time = line.substring(1).trim();  // 나머지를 시간으로 (앞쪽 공백 제거)
                    timeMap.put(day, time);
                }
            }
        }

        return timeMap;
    }

    public static Fitness toEntity(FitnessAdminRequestDTO.FitnessReqDTO dto){
        if(dto.getTotalFee() > dto.getFee()){
            throw new FitnessException(FitnessErrorCode.INVALID_SALE_PRICE);
        }
        return Fitness.builder()
                .name(dto.getFitnessName())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .phoneNumber(dto.getPhoneNumber())
                .fee(dto.getFee())
                .totalFee(dto.getTotalFee())
                .isPurchasable(dto.isPurchasable())
                .notice(dto.getNotice())
                .time(convertMapToFormattedString(dto.getTime()))
                .howToUse(dto.getHowToUse())
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

    public static FitnessAdminResponseDTO.FitnessAdminPreviewDTO toFitnessAdminPreviewDTO(Fitness fitness, String mainImage, List<String> additionalImages) {

        // 카테고리
        List<String> categoryNames = fitness.getCategoryList().stream()
                .map(Category::getCategoryName) // Category 엔티티의 getName() 메서드 호출
                .collect(Collectors.toList());


        return FitnessAdminResponseDTO.FitnessAdminPreviewDTO.builder()
                .id(fitness.getId())
                .loginId(fitness.getOwner().getLoginId())
                .fitnessName(fitness.getName())
                .address(fitness.getAddress())
                .detailAddress(fitness.getDetailAddress())
                .phoneNumber(fitness.getPhoneNumber())
                .fee(fitness.getFee())
                .totalFee(fitness.getTotalFee())
                .categoryList(categoryNames)
                .isPurchasable(fitness.getIsPurchasable())
                .notice(fitness.getNotice())
                .time(FitnessAdminConverter.convertFormattedStringToMap(fitness.getTime()))
                .howToUse(fitness.getHowToUse())
                .longitude(fitness.getLongitude())
                .latitude(fitness.getLatitude())
                .fitnessImage(mainImage)
                .additionalImages(additionalImages)
                .build();
    }
}
