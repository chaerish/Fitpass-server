package com.example.fitpassserver.domain.coin.converter;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.entity.CoinUsageHistory;
import com.example.fitpassserver.domain.fitnessPaymentHistory.entity.FitnessPaymentHistory;

public class CoinUsageHistoryConverter {

    public static CoinUsageHistory toCoinUsageHistory(Coin coin, FitnessPaymentHistory fitnessPaymentHistory, int usageCount) {
        return CoinUsageHistory.builder()
                .usageCount(usageCount)
                .coin(coin)
                .fitnessPaymentHistory(fitnessPaymentHistory)
                .build();
    }
}
