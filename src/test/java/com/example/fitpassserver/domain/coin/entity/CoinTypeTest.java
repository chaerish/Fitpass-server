package com.example.fitpassserver.domain.coin.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CoinTypeTest {

    private Map<String, CoinTypeEntity> testCoinTypeMap;

    @BeforeEach
    public void setUp() {
        testCoinTypeMap = new HashMap<>();
        testCoinTypeMap.put("COIN_1", CoinTypeEntity.builder()
                .coinType(CoinType.COIN_1)
                .name("1코인")
                .price(550)
                .expirationPeriod(30)
                .coinQuantity(1)
                .coinAddition(0)
                .build()
        );

        testCoinTypeMap.put("COIN_10", CoinTypeEntity.builder()
                .coinType(CoinType.COIN_10)
                .name("10코인")
                .price(5500)
                .expirationPeriod(30)
                .coinQuantity(10)
                .coinAddition(0)
                .build()
        );
    }

    @Test
    public void 수량과_가격만으로_판단한다() {
        CoinTypeEntity type1 = testCoinTypeMap.get("COIN_1");
        CoinTypeEntity type2 = testCoinTypeMap.get("COIN_10");

        assertThat(type1.getCoinType()).isEqualTo(CoinType.COIN_1);
        assertThat(type2.getCoinType()).isEqualTo(CoinType.COIN_10);
        assertThat(type1 == type2).isFalse();
    }
}