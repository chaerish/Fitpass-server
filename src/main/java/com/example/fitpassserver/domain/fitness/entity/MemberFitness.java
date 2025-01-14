package com.example.fitpassserver.domain.fitness.entity;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member_fitness")
public class MemberFitness extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "active_time")
    private LocalDateTime activeTime;

    @Column(name = "is_agree", nullable = false)
    private boolean isAgree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fitness_id", nullable = false)
    private Fitness fitness;

    public void use(){
        this.activeTime = LocalDateTime.now();
        this.status = Status.PROGRESS;
    }
}
