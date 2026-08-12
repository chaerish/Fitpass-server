package com.example.fitpassserver.domain.coinPaymentHistory.dto.event;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;

public record CoinApprovedEvent(
        //트랜션 커밋 이후에 처리되는 이벤트이므로, 이벤트에 JPA엔티티를 그대로 담으면 detached Entity 문제가 생긴다. (이전 트랜잭션 상태를 듬)
       Long historyId
) {
}
