package com.example.fitpassserver.domain.coinPaymentHistory.repository;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.PaymentStatus;
import com.example.fitpassserver.domain.member.entity.Member;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinPaymentRepository extends JpaRepository<CoinPaymentHistory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select h from CoinPaymentHistory h where h.id = :id")
    Optional<CoinPaymentHistory> findByIdForUpdate(@Param("id")Long id);

    List<CoinPaymentHistory> findByPaymentStatusInAndCoinIsNull(List<PaymentStatus>statuses);
    Optional<CoinPaymentHistory> findFirst1ByMemberOrderByCreatedAtDesc(Member member);
    Optional<CoinPaymentHistory> findByMemberAndTidAndPaymentStatus(
            Member member,
            String tid,
            PaymentStatus paymentStatus
    );
    Optional<CoinPaymentHistory> findByIdAndPaymentStatus(Long id, PaymentStatus paymentStatus);

    Slice<CoinPaymentHistory> findAllByMemberAndCreatedAtLessThanAndCoinIsNotNullAndPaymentStatusOrderByCreatedAtDesc(
            Member member,
            LocalDateTime createdAt,
            PaymentStatus paymentStatus,
            Pageable pageable
    );

    @Query("SELECT h FROM CoinPaymentHistory h " +
            "WHERE (CASE WHEN h.coin.planType = 'NONE' THEN 'coin' ELSE 'plan' END) = :query " +
            "AND h.createdAt < :createdAt " +
            "AND h.member = :member " +
            "AND h.coin IS NOT NULL " +
            "AND h.paymentStatus = :paymentStatus " +
            "ORDER BY h.createdAt DESC")
    Slice<CoinPaymentHistory> findAllByQueryAndCreatedAtLessThanOrderByCreatedAtDesc(
            @Param("query") String query,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("member") Member member,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            Pageable pageable
    );

    Page<CoinPaymentHistory> findAllByMemberIn(Pageable pageable, List<Member> member);

    Page<CoinPaymentHistory> findAll(Pageable pageable);

    List<CoinPaymentHistory> findByCoinInAndPaymentStatus(List<Coin> coins, PaymentStatus paymentStatus);

    CoinPaymentHistory findByCoinAndPaymentStatus(Coin coin, PaymentStatus paymentStatus);
}
