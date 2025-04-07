package com.example.fitpassserver.owner.fitnessHistory.converter;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.owner.fitnessHistory.dto.RevenueHistoryResponseDTO;
import com.example.fitpassserver.owner.fitnessHistory.dto.RevenueHistoryResponseDTO.RevenueHistoryDetailDTO;
import com.example.fitpassserver.owner.fitnessHistory.dto.UsageHistoryResponseDTO;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class FitnessHistoryConverter {

    public static UsageHistoryResponseDTO to(Page<MemberFitness> memberFitnessPage) {
        return new UsageHistoryResponseDTO(
                memberFitnessPage.stream().map(FitnessHistoryConverter::to).collect(Collectors.toList()),
                memberFitnessPage.getTotalElements(),
                memberFitnessPage.getTotalPages());

    }

    public static UsageHistoryResponseDTO.FitnessUsageDetailDTO to(MemberFitness memberFitness) {
        return new UsageHistoryResponseDTO.FitnessUsageDetailDTO(
                memberFitness.getMember().getLoginId(),
                memberFitness.getMember().getName(),
                memberFitness.getFitness().getTotalFee(),
                memberFitness.getActiveTime()
        );

    }

    public static RevenueHistoryResponseDTO to(Page<Object[]> memberFitnessPage, int units) {
        return new RevenueHistoryResponseDTO(
                memberFitnessPage.stream().map(object -> to(object, units)).collect(Collectors.toList()),
                memberFitnessPage.getTotalElements(),
                memberFitnessPage.getTotalPages());

    }

    public static RevenueHistoryDetailDTO to(Object[] objects, int units) {
        String monthStr = (String) objects[0];
        LocalDateTime latestTime = (LocalDateTime) objects[1];
        Long count = (Long) objects[2];

        String month = monthStr.split("-")[1];
        Long totalPrice = count * units;

        return new RevenueHistoryResponseDTO.RevenueHistoryDetailDTO(
                month,
                latestTime,
                totalPrice
        );
    }
}

