package com.example.fitpassserver.domain.coin.repository;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    List<Coin> findAllByMemberIsAndCountGreaterThanAndExpiredDateGreaterThanOrderByExpiredDateAsc(Member member, Long count, LocalDate expiredDate);
}
