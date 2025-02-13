package com.example.fitpassserver.domain.coinPaymentHistory.repository;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinPaymentRepository extends JpaRepository<CoinPaymentHistory, Long> {
    Optional<CoinPaymentHistory> findFirst1ByMemberOrderByCreatedAtDesc(Member member);

    Page<CoinPaymentHistory> findAllByMemberIn(Pageable pageable, List<Member> member);

    Page<CoinPaymentHistory> findAll(Pageable pageable);

    CoinPaymentHistory findByCoinAndPaymentStatus(Coin coin, PaymentStatus paymentStatus);
}
