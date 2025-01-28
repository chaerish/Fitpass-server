package com.example.fitpassserver.domain.coinPaymentHistory.dto.response;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import lombok.Builder;

import java.util.List;

@Builder
public record CoinPaymentHistoryResponseListDTO(
        List<CoinPaymentHistoryResponseDTO> items,
        boolean isSubscribing,
        boolean hasNext,
        Long cursor,
        int size
) {
    @Builder
    public record CoinPaymentHistoryResponseDTO(
            Long id,
            PlanType planType,
            boolean isAgree,
            Long coinCount,
            Integer price
    ) {
        public static CoinPaymentHistoryResponseDTO toCoinPaymentHistoryResponseDTO(Coin coin) {
            CoinPaymentHistory coinPaymentHistory = coin.getHistory();
            return CoinPaymentHistoryResponseDTO.builder()
                    .id(coinPaymentHistory.getId())
                    .planType(coin.getPlanType())
                    .isAgree(coinPaymentHistory.isAgree())
                    .coinCount(coin.getCount())
                    .price(coinPaymentHistory.getPaymentPrice())
                    .build();
        }
    }
}
