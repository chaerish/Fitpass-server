package com.example.fitpassserver.domain.fitness.repository;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFitnessRepository extends JpaRepository<MemberFitness, Long> {
    List<MemberFitness> findAllByMember(Member member);

    Page<MemberFitness> findAllByMemberIn(Pageable pageable, List<Member> member);

    List<MemberFitness> findByStatusAndActiveTimeBefore(Status status, LocalDateTime time);

    boolean existsByMemberAndStatusIn(Member member, List<Status> status);

    int countAllByCreatedAtGreaterThanEqualAndCreatedAtLessThan(LocalDateTime greaterThan, LocalDateTime lessThan);

    int countAllByActiveTimeGreaterThanEqualAndActiveTimeLessThan(LocalDateTime greaterThan, LocalDateTime lessThan);
}
