package com.example.fitpassserver.domain.coin.repository;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.member.entity.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    List<Coin> findAllByMemberIsAndCountGreaterThanAndExpiredDateGreaterThanOrderByExpiredDateAsc(Member member,
                                                                                                  Long count,
                                                                                                  LocalDate expiredDate);

    List<Coin> findAllByMemberAndExpiredDateGreaterThanEqual(Member member, LocalDate now);

    @Query("SELECT c FROM Coin c WHERE c.member = :member AND c.expiredDate < :now")
    List<Coin> findExpiredCoinsByMember(@Param("member") Member member, @Param("now") LocalDate now);

    @Query("SELECT c FROM Coin c " +
            "WHERE (CASE WHEN c.planType = 'NONE' THEN 'NONE' ELSE 'NOT NONE' END) = " +
            "(CASE WHEN :query = 'coin' THEN 'NONE' ELSE 'NOT NONE' END) " +
            "AND c.createdAt < :createdAt AND c.member = :member " +
            "ORDER BY c.createdAt DESC")
    Slice<Coin> findAllByQueryIsCreatedAtLessThanOrderByCreatedAtDesc(@Param("query") String query,
                                                                      @Param("createdAt") LocalDateTime createAt,
                                                                      @Param("member") Member member,
                                                                      Pageable pageable);

    Slice<Coin> findAllByHistoryCreatedAtLessThanAndMemberIsOrderByCreatedAtDesc(LocalDateTime createAt, Member member,
                                                                                 Pageable pageable);

}
