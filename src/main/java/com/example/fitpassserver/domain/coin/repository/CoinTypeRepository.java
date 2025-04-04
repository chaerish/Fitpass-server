package com.example.fitpassserver.domain.coin.repository;

import com.example.fitpassserver.domain.coin.entity.CoinType;
import com.example.fitpassserver.domain.coin.entity.CoinTypeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinTypeRepository extends JpaRepository<CoinTypeEntity, Long> {
    Optional<CoinTypeEntity> findByCoinType(CoinType CoinType);

    Optional<CoinTypeEntity> findByName(String name);

    @Query("SELECT c FROM CoinTypeEntity c ORDER BY c.coinQuantity ASC")
    List<CoinTypeEntity> findAllSortedByCoinQuantity();

    Optional<CoinTypeEntity> findByPrice(int price);

    Optional<CoinTypeEntity> findByCoinQuantity(int quantity);

}