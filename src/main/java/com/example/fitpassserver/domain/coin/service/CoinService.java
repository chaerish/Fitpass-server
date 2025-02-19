package com.example.fitpassserver.domain.coin.service;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.entity.CoinTypeEntity;
import com.example.fitpassserver.domain.coin.exception.CoinErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinException;
import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.coin.repository.CoinTypeRepository;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.entity.PlanTypeEntity;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanTypeRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final CoinRepository coinRepository;
    private final PlanTypeRepository planTypeRepository;
    private final CoinTypeRepository coinTypeRepository;
    private final int DEAD_LINE = 30;

    //결제 성공시 Coin 엔티티 증가
    public Coin createNewCoin(Member member, CoinPaymentHistory history, KakaoPaymentApproveDTO dto) {
        int price = dto.amount().total();
        int quantity = dto.quantity();

        CoinTypeEntity coinType = coinTypeRepository.findByPrice(price)
                .orElseThrow(() -> new CoinException(CoinErrorCode.COIN_NOT_FOUND));

        if (coinType == null) {
            throw new CoinException(CoinErrorCode.COIN_NOT_FOUND);
        }
        if (!history.getMember().getId().equals(member.getId())) {
            throw new CoinException(CoinErrorCode.COIN_UNAUTHORIZED_ERROR);
        }
        return coinRepository.save(Coin.builder()
                .member(member)
                .count(((long) coinType.getCoinQuantity() * quantity))
                .expiredDate(LocalDate.now().plusDays(coinType.getExpirationPeriod()))
                .history(history)
                .planType(PlanType.NONE)
                .build());
    }

    @Transactional
    public Coin createSubscriptionNewCoin(Member member, CoinPaymentHistory history, Plan plan) {
        if (!history.getMember().getId().equals(member.getId())) {
            throw new CoinException(CoinErrorCode.COIN_UNAUTHORIZED_ERROR);
        }
        PlanType planType = plan.getPlanType();
        PlanTypeEntity planTypeEntity = planTypeRepository.findByPlanType(planType)
                .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

        return coinRepository.save(Coin.builder()
                .member(member)
                .planType(planType)
                .count((long) planTypeEntity.getCoinQuantity())
                .expiredDate(LocalDate.now().plusDays(DEAD_LINE))
                .history(history)
                .build());
    }
}
