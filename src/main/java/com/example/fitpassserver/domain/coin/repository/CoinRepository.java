package com.example.fitpassserver.domain.coin.repository;

import com.example.fitpassserver.domain.coin.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Long> {

}
