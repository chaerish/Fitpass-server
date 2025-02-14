package com.example.fitpassserver.domain.coin.entity;

import com.example.fitpassserver.domain.coin.exception.CoinErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CoinType {
    COIN_1("1코인"),
    COIN_5("5코인"),
    COIN_10("10코인"),
    COIN_20("20코인"),
    COIN_30("30코인"),
    COIN_180("180코인"),
    COIN_300("300코인");
    private String name;

    public static CoinType getCoinType(String name) {
        return Arrays.stream(values())
                .filter(coinType -> coinType.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new CoinException(CoinErrorCode.COIN_NOT_FOUND));
    }
}
