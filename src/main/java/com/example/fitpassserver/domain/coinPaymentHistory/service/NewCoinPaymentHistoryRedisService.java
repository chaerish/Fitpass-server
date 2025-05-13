package com.example.fitpassserver.domain.coinPaymentHistory.service;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PaymentSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewCoinPaymentHistoryRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void savePaymentInfo(String paymentId, Long memberId, String itemId, int price) {
        PaymentSession session = new PaymentSession(paymentId, memberId, itemId, price);

        try {
            String sessionJson = objectMapper.writeValueAsString(session);
            redisTemplate.opsForValue().set(buildKey(paymentId), sessionJson, Duration.ofMinutes(30));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis 저장 실패: " + e.getMessage());
        }
    }

    public PaymentSession getPaymentInfo(String paymentId) {
        String sessionJson = (String) redisTemplate.opsForValue().get(buildKey(paymentId));
        if (sessionJson == null) {
            return null;
        }
        try {
            return objectMapper.readValue(sessionJson, PaymentSession.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis 조회 실패: " + e.getMessage());
        }
    }

    public void deletePaymentInfo(String paymentId) {
        redisTemplate.delete(buildKey(paymentId));
    }

    private String buildKey(String paymentId) {
        return "payment:" + paymentId;
    }
}
