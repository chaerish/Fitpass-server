package com.example.fitpassserver.domain.fitness.repository;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberFitnessRepository extends JpaRepository<MemberFitness, Long>, MemberFitnessCustomRepository {
    List<MemberFitness> findAllByMember(Member member);

    List<MemberFitness> findByStatusAndActiveTimeBefore(Status status, LocalDateTime time);

    boolean existsByMemberAndStatusIn(Member member, List<Status> status);

    boolean existsByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    int countAllByCreatedAtGreaterThanEqualAndCreatedAtLessThan(LocalDateTime greaterThan, LocalDateTime lessThan);

    int countAllByActiveTimeGreaterThanEqualAndActiveTimeLessThan(LocalDateTime greaterThan, LocalDateTime lessThan);

    List<MemberFitness> findAllByStatusIsAndCreatedAtLessThan(Status status, LocalDateTime createdAt);
}
