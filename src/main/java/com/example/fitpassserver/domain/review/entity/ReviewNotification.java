package com.example.fitpassserver.domain.review.entity;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review_notification")
public class ReviewNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private MemberFitness memberFitness;

    private LocalDateTime notifyAt; // 알림 예정 시간

    private boolean sent = false;

    public void setMemberFitness (MemberFitness memberFitness) {
        this.memberFitness = memberFitness;
    }

    public void setNotifyAt(LocalDateTime notifyAt){
        this.notifyAt = notifyAt;
    }

    public void setSent(boolean sent){
        this.sent = sent;
    }
}
