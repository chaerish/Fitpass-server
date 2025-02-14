package com.example.fitpassserver.domain.plan.repository;

import com.example.fitpassserver.domain.plan.entity.PlanType;
import com.example.fitpassserver.domain.plan.entity.PlanTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanTypeRepository extends JpaRepository<PlanTypeEntity, Long> {
    Optional<PlanTypeEntity> findByPlanType(PlanType planType);

    @Query("SELECT c FROM PlanTypeEntity c ORDER BY c.coinQuantity ASC")
    List<PlanTypeEntity> findAllSortedByCoinQuantity();

    ;

}