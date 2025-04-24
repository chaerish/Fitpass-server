package com.example.fitpassserver.domain.plan.service;

import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayErrorCode;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayException;
import com.example.fitpassserver.domain.plan.dto.request.NotificationInfo;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveTid(String userId, String tid) {
        redisTemplate.opsForValue().set("plan_tid" + userId, tid, Duration.ofMinutes(10));
    }

    public String getTid(String userId) {
        String tid = (String) redisTemplate.opsForValue().get("plan_tid" + userId);
        if (tid == null || tid.isEmpty() || tid.isBlank()) {
            throw new KakaoPayException(KakaoPayErrorCode.NO_TID_ERROR);
        }
        return tid;
    }

    public void deleteTid(String userId) {
        redisTemplate.delete("plan_tid" + userId);
    }

    public void saveSuccessNotification(String planId, NotificationInfo info) {
        String key = "success_notification:" + planId;
        redisTemplate.opsForValue().set(key, info, Duration.ofHours(12)); // 00시 저장 → 12시 만료
    }

    public List<NotificationInfo> getAllSuccessNotifications() {
        Set<String> keys = redisTemplate.keys("success_notification:*");
        if (keys == null) {
            return List.of();
        }

        List<NotificationInfo> result = new ArrayList<>();
        for (String key : keys) {
            NotificationInfo info = (NotificationInfo) redisTemplate.opsForValue().get(key);
            if (info != null) {
                result.add(info);
            }
        }
        return result;
    }

    public void deleteSuccessNotification(String planId) {
        redisTemplate.delete("success_notification:" + planId);
    }
}
