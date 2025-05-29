package com.example.fitpassserver.domain.plan.service.planLog;

import com.example.fitpassserver.domain.plan.entity.planLog.LogType;
import com.example.fitpassserver.domain.plan.entity.planLog.NotificationLog;
import com.example.fitpassserver.domain.plan.repository.NotificationLogRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationLogService {
    private final NotificationLogRepository notificationLogRepository;


    // 알림톡 동시에 객체 생성
    public NotificationLog createNewNotification(LogType logType) {
        NotificationLog notificationLog = NotificationLog.builder()
                .sentAt(LocalDateTime.now())
                .logType(logType)
                .isSent(true)
                .build();
        notificationLogRepository.save(notificationLog);
        return notificationLog;
    }

    // 실패 알림톡 동시에 생성
    public NotificationLog createFailNotification(LogType logType) {
        NotificationLog notificationLog = NotificationLog.builder()
                .sentAt(LocalDateTime.now())
                .logType(logType)
                .isSent(false)
                .build();
        notificationLogRepository.save(notificationLog);
        return notificationLog;
    }

}
