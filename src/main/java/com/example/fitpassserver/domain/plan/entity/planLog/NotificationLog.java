package com.example.fitpassserver.domain.plan.entity.planLog;

import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "notification_log")
public class NotificationLog extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    @Column(name = "is_sent")
    private boolean isSent;

    @Column(name = "log_type")
    @Enumerated(EnumType.STRING)
    LogType logType;

    public void completedNotification(CancelType cancelType) {
        updateCancelSentAt(LocalDateTime.now());
        setIsSent(true);
    }

    private void updateCancelSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    private void setIsSent(boolean sent) {
        isSent = sent;
    }

}
