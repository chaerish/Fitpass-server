package com.example.fitpassserver.domain.coinPaymentHistory.service;

import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayErrorCode;
import com.example.fitpassserver.domain.coinPaymentHistory.exception.KakaoPayException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinPaymentHistoryRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveTid(String userId, String tid) {
        redisTemplate.opsForValue().set("coin_tid" + userId, tid, Duration.ofMinutes(10));
    }

    public String getTid(String userId) {
        String tid = (String) redisTemplate.opsForValue().get("coin_tid" + userId);
        if (tid == null || tid.isEmpty() || tid.isBlank()) {
            throw new KakaoPayException(KakaoPayErrorCode.NO_TID_ERROR);
        }
        return tid;
    }

    public void deleteTid(String userId) {
        redisTemplate.delete("coin_tid" + userId);
    }
}
