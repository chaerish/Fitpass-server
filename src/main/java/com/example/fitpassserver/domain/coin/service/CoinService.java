package com.example.fitpassserver.domain.coin.service;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.entity.CoinType;
import com.example.fitpassserver.domain.coin.exception.CoinErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinException;
import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final CoinRepository coinRepository;

    //결제 성공시 Coin 엔티티 증가
    public void createNewCoin(Member member, CoinPaymentHistory history, KakaoPaymentApproveDTO dto) {
        int price = dto.amount().total();
        int quantity = dto.quantity();
        CoinType type = CoinType.getCoinType(price, quantity);
        if (type == null) {
            throw new CoinException(CoinErrorCode.COIN_NOT_FOUND);
        }
        if (!history.getMember().getId().equals(member.getId())) {
            throw new CoinException(CoinErrorCode.COIN_UNAUTHORIZED_ERROR);
        }
        coinRepository.save(Coin.builder()
                .member(member)
                .count((long) quantity)
                .expiredDate(LocalDate.now().plusDays(type.getDeadLine()))
                .history(history)
                .planType(PlanType.NONE)
                .build());
    }

    public void createSubscriptionNewCoin(Member member, CoinPaymentHistory history, String itemName) {
        PlanType type = PlanType.getPlanType(itemName);
        if (type == null) {
            throw new PlanException(PlanErrorCode.PLAN_NOT_FOUND);
        }
        if (!history.getMember().getId().equals(member.getId())) {
            throw new CoinException(CoinErrorCode.COIN_UNAUTHORIZED_ERROR);
        }
        coinRepository.save(Coin.builder()
                .member(member)
                .planType(type)
                .count((long) type.getCoinQuantity())
                .expiredDate(LocalDate.now().plusDays(30))
                .history(history)
                .build());
    }
}
