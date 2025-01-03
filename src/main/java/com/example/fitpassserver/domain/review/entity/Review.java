package com.example.fitpassserver.domain.review.entity;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="content",nullable = false)
    private String content;

    @Column(name="score",nullable = false)
    private double score;

    @Column(name = "is_agree", nullable = false)
    private boolean isAgree;

    @OneToOne
    @JoinColumn(name = "member_fitness_id", nullable = false)
    private MemberFitness memberFitness;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fitness_id", nullable = false)
    private Fitness fitness;
}
