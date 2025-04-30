package com.example.fitpassserver.domain.plan.entity.planLog;

import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "plan_attempt_log")
public class PlanAttemptLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Plan plan;
    @Column(name = "attempt_order")
    private int attemptOrder; // 1 = 1차 시도, 2 = 2차 시도
    @Column(name = "first_attempted_at")
    private LocalDateTime firstAttemptedAt;
    @Column(name = "last_attempted_at")
    private LocalDateTime lastAttemptedAt;
    @Column(name = "is_completed")
    private boolean isCompleted;
    @OneToOne
    @JoinColumn(name = "notificatiaon_log")
    private NotificationLog notificationLog;

    private void addAttemptOrder() {
        this.attemptOrder += 1;
    }

    public void updateAttemptFrequency() {
        this.lastAttemptedAt = LocalDateTime.now();
        addAttemptOrder();
        cancelCompleted();
    }

    public void cancelCompleted() {
        this.isCompleted = true;
    }

    public void setNotificationLog(NotificationLog notificationLog) {
        this.notificationLog = notificationLog;
    }
}
