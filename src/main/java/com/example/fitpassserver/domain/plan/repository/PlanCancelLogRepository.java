package com.example.fitpassserver.domain.plan.repository;

import com.example.fitpassserver.domain.plan.entity.planLog.CancelType;
import com.example.fitpassserver.domain.plan.entity.planLog.PlanCancelLog;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanCancelLogRepository extends JpaRepository<PlanCancelLog, Long> {
    @EntityGraph(attributePaths = {
            "planAttemptLog",
            "planAttemptLog.plan",
            "planAttemptLog.plan.member"
    })
    List<PlanCancelLog> findByCanceledAtBetweenAndCancelType(LocalDateTime start, LocalDateTime now,
                                                             CancelType cancelType);
}
