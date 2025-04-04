package com.example.fitpassserver.owner.fitness.converter;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.owner.fitness.dto.FitnessOwnerResDTO;
import org.springframework.data.domain.Slice;

import java.util.List;

public class FitnessOwnerConverter {

    public static FitnessOwnerResDTO.FitnessPreviewDTO toFitnessPreviewDTO(Fitness fitness) {
        return FitnessOwnerResDTO.FitnessPreviewDTO.builder()
                .fitnessId(fitness.getId())
                .fitnessName(fitness.getName())
                .address(fitness.getAddress())
                .imageUrl(fitness.getFitnessImage())
                .build();

    }

    public static FitnessOwnerResDTO.FitnessListDTO toFitnessPageResDTO(Slice<Fitness> fitnesses) {
        List<FitnessOwnerResDTO.FitnessPreviewDTO> fitnessOwnerResDTOs = fitnesses.getContent().stream()
                .map(FitnessOwnerConverter::toFitnessPreviewDTO).toList();

        // 다음 커서 계산 로직 수정
        Long nextCursor = 0L;
        if (fitnesses.hasNext() && !fitnesses.getContent().isEmpty()) {
            // 마지막 항목의 ID를 다음 커서로 사용
            nextCursor = fitnesses.getContent().get(fitnesses.getContent().size() - 1).getId();
        }

        return FitnessOwnerResDTO.FitnessListDTO.builder()
                .hasNext(fitnesses.hasNext())
                .nextCursor(nextCursor)
                .fitnessList(fitnessOwnerResDTOs)
                .build();
    }
}
