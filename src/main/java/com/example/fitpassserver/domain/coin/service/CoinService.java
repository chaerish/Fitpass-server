package com.example.fitpassserver.domain.coin.service;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.entity.CoinType;
import com.example.fitpassserver.domain.coin.exception.CoinErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinException;
import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.member.entity.Member;
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
        Long quantity = (long) dto.quantity();
        CoinType type = CoinType.getCoinType(price);
        if (type == null) {
            throw new CoinException(CoinErrorCode.COIN_NOT_FOUND);
        }
        if (!history.getMember().equals(member)) {
            throw new CoinException(CoinErrorCode.COIN_UNAUTHORIZED_ERROR);
        }
        coinRepository.save(Coin.builder()
                .member(member)
                .count(quantity)
                .expiredDate(LocalDate.now().plus(type.getDeadLine()))
                .history(history)
                .build());
    }
}
