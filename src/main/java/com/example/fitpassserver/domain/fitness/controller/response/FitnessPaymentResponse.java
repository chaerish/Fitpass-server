package com.example.fitpassserver.domain.fitness.controller.response;


import com.example.fitpassserver.domain.fitness.entity.Fitness;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FitnessPaymentResponse {
    private Long id;
    private String fitnessName;
    private String imageUrl;
    private Integer fee;
    private Integer discount;
    private Integer totalFee;
    private Long feeBeforePay;
    private Long feeAfterPay;

    public static FitnessPaymentResponse toFitnessPaymentResponse(Fitness fitness, Long coin, String imageUrl) {
        Long feeAfterPay = coin - fitness.getTotalFee();
        if(feeAfterPay < 0L){
            feeAfterPay = 0L;
        }
        return FitnessPaymentResponse.builder()
                .id(fitness.getId())
                .fitnessName(fitness.getName())
                .imageUrl(imageUrl)
                .fee(fitness.getFee())
                .discount(fitness.getDiscount())
                .totalFee(fitness.getTotalFee())
                .feeBeforePay(coin)
                .feeAfterPay(feeAfterPay)
                .build();
    }
}
