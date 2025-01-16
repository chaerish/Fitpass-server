package com.example.fitpassserver.domain.plan.entity;

public enum PlanType {
    NONE("플랜 없음", 0, 0),
    BASIC("베이직", 50000, 90),
    STANDARD("스탠다드", 70000, 135),
    PRO("프로", 100000, 200);

    PlanType(String name, int price, int coinQuantity) {
        this.name = name;
        this.price = price;
        this.coinQuantity = coinQuantity;
    }

    private String name;
    private int price;
    private int coinQuantity;

    public static PlanType getPlanType(String name) {
        PlanType type = null;
        for (PlanType planType : PlanType.values()) {
            if (planType.name.equals(name)) {
                return planType;
            }

        }
        return type;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getCoinQuantity() {
        return coinQuantity;
    }
}
