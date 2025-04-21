package com.example.fitpassserver.domain.plan.repository;

import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.planLog.PlanAttemptLog;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanAttemptLogRepository extends JpaRepository<PlanAttemptLog, Long> {
    PlanAttemptLog findByPlanAndCreatedAtBetween(Plan plan, LocalDateTime start, LocalDateTime end);

    @EntityGraph(attributePaths = {"plan", "plan.member"})
    @Query("SELECT l FROM PlanAttemptLog l WHERE l.firstAttemptedAt BETWEEN :start AND :end AND l.attemptOrder = 1 AND l.isCompleted = false")
    List<PlanAttemptLog> findFailureNotificationTarget(LocalDateTime start, LocalDateTime end);

    boolean existsByPlanAndCreatedAtBetween(Plan plan, LocalDateTime start, LocalDateTime end);
}
