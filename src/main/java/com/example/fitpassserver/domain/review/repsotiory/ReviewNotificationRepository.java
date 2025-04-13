package com.example.fitpassserver.domain.review.repsotiory;

import com.example.fitpassserver.domain.review.entity.ReviewNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewNotificationRepository extends JpaRepository<ReviewNotification, Long> {
    List<ReviewNotification> findBySentFalseAndNotifyAtBefore(LocalDateTime now);
}
