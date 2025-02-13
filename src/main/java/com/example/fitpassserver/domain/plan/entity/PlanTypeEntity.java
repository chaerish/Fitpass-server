package com.example.fitpassserver.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "plan_type")
public class PlanTypeEntity {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false, unique = true)
    private PlanType planType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "coin_quantity", nullable = false) //지급 코인
    private int coinQuantity;

    @Column(name = "coin_addition", nullable = false) //추가 코인
    private int coinAddition;

    @Column(name = "expiration_period", nullable = false) //유효기간
    private String expirationPeriod;


    public void updatePlan(String name, int price, int coinQuantity, int coinAddition, String expirationPeriod) {
        this.name = name;
        this.price = price;
        this.coinQuantity = coinQuantity;
        this.coinAddition = coinAddition;
        this.expirationPeriod = expirationPeriod;
    }
}