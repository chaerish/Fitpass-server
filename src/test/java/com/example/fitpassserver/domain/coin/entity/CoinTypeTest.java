package com.example.fitpassserver.domain.coin.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CoinTypeTest {
    @Test
    public void 수량과_가격만으로_판단한다() {
        CoinType type1 = CoinType.getCoinType(5500, 10); //5500
        CoinType type2 = CoinType.getCoinType(5500, 1); //5500
        Assertions.assertThat(type1).isEqualTo(CoinType.COIN_1);
        Assertions.assertThat(type2).isEqualTo(CoinType.COIN_10);
        Assertions.assertThat(type1 == type2).isFalse();
    }

}