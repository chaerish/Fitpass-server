package com.example.fitpassserver.domain.plan.entity.planLog;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "plan_cancel_log")
public class PlanCancelLog extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name = "plan_attempt_log")
    private PlanAttemptLog planAttemptLog;
    @Column(name = "plan_name")
    private String planName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancel_member")
    private Member member;
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;
    @Column(name = "cancel_type")
    @Enumerated(EnumType.STRING)
    private CancelType cancelType;
    @Column(name = "cancel_amount")
    private int cancelAmount;
    @OneToOne
    @JoinColumn(name = "notification_log")
    private NotificationLog notificationLog;

    public void setNotificationLog(NotificationLog notificationLog) {
        this.notificationLog = notificationLog;
    }
}
