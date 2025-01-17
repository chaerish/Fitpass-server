package com.example.fitpassserver.domain.plan.repository;

import com.example.fitpassserver.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByMemberId(Long memberId);
}
