package com.example.fitpassserver.domain.plan.repository;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findAllByPlanDateBefore(LocalDate time);

    Optional<Plan> findByMember(Member member);

    Optional<Plan> findByMemberId(Long memberId);

    boolean existsByMemberAndPlanTypeNotAndPlanTypeIsNotNull(Member member, PlanType planType);
}
