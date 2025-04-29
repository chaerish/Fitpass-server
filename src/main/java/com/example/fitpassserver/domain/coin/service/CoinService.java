package com.example.fitpassserver.domain.coin.service;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.entity.CoinType;
import com.example.fitpassserver.domain.coin.entity.CoinTypeEntity;
import com.example.fitpassserver.domain.coin.exception.CoinErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinException;
import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.coin.repository.CoinTypeRepository;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.event.CoinPaymentAllSuccessEvent;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.repository.CoinPaymentRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.entity.PlanTypeEntity;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanTypeRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final CoinRepository coinRepository;
    private final CoinPaymentRepository coinPaymentRepository;
    private final PlanTypeRepository planTypeRepository;
    private final CoinTypeRepository coinTypeRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final int DEAD_LINE = 30;

    //결제 성공시 Coin 엔티티 증가

    public Coin createNewCoinByKakaoPay(Member member, KakaoPaymentApproveDTO dto) {
        return createNewCoin(member, dto.amount().total());
    }

    public Coin createNewCoinByPg(Member member, String paymentId, int price) {
        return createNewCoin(member, price);
    }

    public Coin createNewCoinByReview(Member member) {
        CoinTypeEntity coinType = coinTypeRepository.findByCoinType(CoinType.COIN_1)
                .orElseThrow(() -> new CoinException(CoinErrorCode.COIN_NOT_FOUND));

        return createNewCoin(member, coinType.getPrice());
    }

    @Transactional
    public void setCoinAndCoinPayment(Coin coin, CoinPaymentHistory history) {
        coin.setHistory(history);
        coinRepository.save(coin);
        eventPublisher.publishEvent(
                new CoinPaymentAllSuccessEvent(coin.getMember().getPhoneNumber(),
                        history.getCoinCount(),
                        history.getPaymentPrice(), history.getPaymentMethod()));
    }

    @Transactional
    public void setCoinAndCoinPaymentByScheduler(Coin coin, CoinPaymentHistory history) {
        coin.setHistory(history);
        coinRepository.save(coin);
    }


    public Coin createSubscriptionNewCoin(Member member, Plan plan) {
        PlanType planType = plan.getPlanType();
        PlanTypeEntity planTypeEntity = planTypeRepository.findByPlanType(planType)
                .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

        return coinRepository.save(Coin.builder()
                .member(member)
                .planType(planType)
                .count((long) planTypeEntity.getCoinQuantity())
                .expiredDate(LocalDate.now().plusDays(DEAD_LINE))
                .build());
    }

    private Coin createNewCoin(Member member, int price) {
        CoinTypeEntity coinType = coinTypeRepository.findByPrice(price)
                .orElseThrow(() -> new CoinException(CoinErrorCode.COIN_NOT_FOUND));

        if (coinType == null) {
            throw new CoinException(CoinErrorCode.COIN_NOT_FOUND);
        }
        return coinRepository.save(Coin.builder()
                .member(member)
                .count(((long) coinType.getCoinQuantity()))
                .expiredDate(LocalDate.now().plusDays(coinType.getExpirationPeriod()))
                .planType(PlanType.NONE)
                .build());
    }
}
