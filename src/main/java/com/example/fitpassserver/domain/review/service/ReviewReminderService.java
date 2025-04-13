package com.example.fitpassserver.domain.review.service;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.review.entity.ReviewNotification;
import com.example.fitpassserver.domain.review.repsotiory.ReviewNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ReviewReminderService {

    private final ReviewNotificationRepository reviewNotificationRepository;

    public void reserve(MemberFitness memberFitness) {
        ReviewNotification notification = new ReviewNotification();
        notification.setMemberFitness(memberFitness);
        notification.setNotifyAt(LocalDateTime.now().plusHours(1));
        reviewNotificationRepository.save(notification);
    }
}
