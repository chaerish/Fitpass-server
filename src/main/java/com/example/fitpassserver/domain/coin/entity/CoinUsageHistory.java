package com.example.fitpassserver.domain.coin.entity;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitnessPaymentHistory.entity.FitnessPaymentHistory;
import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "coin_usage_history")
public class CoinUsageHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="usage_count",nullable = false)
    private int usageCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id", nullable = false)
    private Coin coin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fitness_payment_history_id", nullable = false)
    private FitnessPaymentHistory fitnessPaymentHistory;
}
