package com.example.fitpassserver.domain.fitness.repository;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberFitnessRepository extends JpaRepository<MemberFitness, Long> {
    List<MemberFitness> findAllByMember(Member member);

    List<MemberFitness> findByStatusAndActiveTimeBefore(Status status, LocalDateTime time);

    boolean existsByMemberAndStatusIsNot(Member member, Status status);

    int countAllByCreatedAtGreaterThanEqualAndCreatedAtLessThan(LocalDateTime greaterThan, LocalDateTime lessThan);

    int countAllByActiveTimeGreaterThanEqualAndActiveTimeLessThan(LocalDateTime greaterThan, LocalDateTime lessThan);
}
