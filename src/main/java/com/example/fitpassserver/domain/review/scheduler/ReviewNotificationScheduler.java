package com.example.fitpassserver.domain.review.scheduler;

import com.example.fitpassserver.domain.kakaoNotice.util.KakaoAlimtalkUtil;
import com.example.fitpassserver.domain.review.entity.ReviewNotification;
import com.example.fitpassserver.domain.review.repsotiory.ReviewNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
@Transactional
public class ReviewNotificationScheduler {

    private final ReviewNotificationRepository reviewNotificationRepository;
    private final KakaoAlimtalkUtil kakaoAlimtalkUtil;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void sendReminders() {
        List<ReviewNotification> targets =
                reviewNotificationRepository.findBySentFalseAndNotifyAtBefore(LocalDateTime.now());

        targets.stream()
                .peek(notification -> kakaoAlimtalkUtil.sendReviewNotice(notification.getMemberFitness().getMember().getPhoneNumber()))
                .forEach(notification -> notification.setSent(true)); // 중복 전송 방지
    }
}

