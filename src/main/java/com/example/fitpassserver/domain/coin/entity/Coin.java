package com.example.fitpassserver.domain.coin.entity;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.entity.PlanName;
import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "coin")
public class Coin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_name", nullable = false)
    private PlanName planName;

    @Column(name = "count", nullable = false)
    private Long count;

    @Column(name="expired_date", nullable = false)
    private LocalDate expiredDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
