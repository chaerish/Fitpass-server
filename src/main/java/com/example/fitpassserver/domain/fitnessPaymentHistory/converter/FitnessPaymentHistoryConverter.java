package com.example.fitpassserver.domain.fitnessPaymentHistory.converter;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitnessPaymentHistory.entity.FitnessPaymentHistory;

public class FitnessPaymentHistoryConverter {

    public static FitnessPaymentHistory toEntity(MemberFitness memberFitness) {
        return FitnessPaymentHistory.builder()
                .memberFitness(memberFitness)
                .build();
    }
}
