package com.example.fitpassserver.domain.coinPaymentHistory.dto.event;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;

public record CoinApprovedEvent(
        CoinPaymentHistory history,
        Coin coin
) {
}
