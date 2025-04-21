package com.example.fitpassserver.domain.plan.repository;

import com.example.fitpassserver.domain.plan.entity.planLog.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

}
