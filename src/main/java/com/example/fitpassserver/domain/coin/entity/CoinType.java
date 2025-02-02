package com.example.fitpassserver.domain.coin.entity;

public enum CoinType {
    COIN_1("코인 1개", 550, 30, 1),
    COIN_5("코인 5개", 2750, 30, 5),
    COIN_10("코인 10개", 5500, 30, 10),
    COIN_20("코인 20개", 11000, 30, 20),
    COIN_30("코인 30개", 16500, 30, 30),
    COIN_180("코인 180개", 99000, 90, 180),
    COIN_300("코인 300개", 165000, 180, 300);
    private String description;
    private int price;
    private int deadLine;
    private int count;

    CoinType(String description, int price, int deadLine, int count) {
        this.description = description;
        this.price = price;
        this.deadLine = deadLine;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public long getDeadLine() {
        return deadLine;
    }

    public static CoinType getCoinType(int price, int quantity) {
        int value = price / quantity;
        CoinType unit = null;
        for (CoinType coinType : CoinType.values()) {
            if (coinType.price == value) {
                unit = coinType;
                break;
            }
        }
        return unit;
    }
}
