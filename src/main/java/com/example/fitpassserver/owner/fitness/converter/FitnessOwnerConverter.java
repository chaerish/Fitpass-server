package com.example.fitpassserver.owner.fitness.converter;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.owner.fitness.dto.request.FitnessOwnerRequestDTO;
import com.example.fitpassserver.owner.fitness.dto.response.FitnessOwnerResponseDTO;
import org.springframework.data.domain.Slice;

import java.util.List;

import static com.example.fitpassserver.admin.fitness.converter.FitnessAdminConverter.convertMapToFormattedString;

public class FitnessOwnerConverter {

    public static FitnessOwnerResponseDTO.FitnessPreviewDTO toFitnessPreviewDTO(Fitness fitness) {
        return FitnessOwnerResponseDTO.FitnessPreviewDTO.builder()
                .fitnessId(fitness.getId())
                .fitnessName(fitness.getName())
                .address(fitness.getAddress())
                .imageUrl(fitness.getFitnessImage())
                .build();

    }

    public static FitnessOwnerResponseDTO.FitnessListDTO toFitnessPageResDTO(Slice<Fitness> fitnesses) {
        List<FitnessOwnerResponseDTO.FitnessPreviewDTO> fitnessOwnerResDTOs = fitnesses.getContent().stream()
                .map(FitnessOwnerConverter::toFitnessPreviewDTO).toList();

        // 다음 커서 계산 로직 수정
        Long nextCursor = 0L;
        if (fitnesses.hasNext() && !fitnesses.getContent().isEmpty()) {
            // 마지막 항목의 ID를 다음 커서로 사용
            nextCursor = fitnesses.getContent().get(fitnesses.getContent().size() - 1).getId();
        }

        return FitnessOwnerResponseDTO.FitnessListDTO.builder()
                .hasNext(fitnesses.hasNext())
                .nextCursor(nextCursor)
                .fitnessList(fitnessOwnerResDTOs)
                .build();
    }
    public static Fitness toEntity(FitnessOwnerRequestDTO.FitnessRequestDTO dto){
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
}
