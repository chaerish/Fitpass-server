package com.example.fitpassserver.domain.fitness.controller.response;


import com.example.fitpassserver.domain.fitness.entity.Fitness;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FitnessPaymentResponse {
    private Long id;
    private String name;
    private String fitnessImage;
    private Integer fee;
    private Integer discount;
    private Integer totalFee;
    private Long feeBeforePay;
    private Long feeAfterPay;

    public static FitnessPaymentResponse toFitnessPaymentResponse(Fitness fitness, Long coin) {
        int totalFee = fitness.getFee() - fitness.getDiscount();
        return FitnessPaymentResponse.builder()
                .id(fitness.getId())
                .name(fitness.getName())
                .fitnessImage(fitness.getFitnessImage())
                .fee(fitness.getFee())
                .discount(fitness.getDiscount())
                .totalFee(totalFee)
                .feeBeforePay(coin)
                .feeAfterPay(coin - totalFee)
                .build();
    }
}
