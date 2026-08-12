package com.example.fitpassserver.domain.coinPaymentHistory.service.redis;

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
        Boolean saved = redisTemplate.opsForValue().setIfAbsent("coin_tid" + userId, tid, Duration.ofMinutes(15));
        //SETNX: setifAbsent은 같은 유저의 중복 ready 덮어쓰기 방지.

        //저장실패: 이미 coin_tid존재 의미, 새결제 요청을 막는다. NPE방지.
        if(Boolean.FALSE.equals(saved)){
            throw new KakaoPayException(KakaoPayErrorCode.ALREADY_READY_PAYMENT);
        }
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
