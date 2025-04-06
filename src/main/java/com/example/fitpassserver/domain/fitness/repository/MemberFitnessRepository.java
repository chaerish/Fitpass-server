package com.example.fitpassserver.domain.fitness.repository;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberFitnessRepository extends JpaRepository<MemberFitness, Long>, MemberFitnessCustomRepository {
    @Query("""
                SELECT FUNCTION('DATE_FORMAT', mf.createdAt, '%Y-%m'), MAX(mf.createdAt), COUNT(mf)
                FROM MemberFitness mf
                WHERE mf.fitness.id = :fitnessId
                GROUP BY FUNCTION('DATE_FORMAT', mf.createdAt, '%Y-%m')
                ORDER BY FUNCTION('DATE_FORMAT', mf.createdAt, '%Y-%m') DESC
            """)
    Page<Object[]> findMonthlyRevenueSummary(@Param("fitnessId") Long fitnessId, Pageable pageable);

    Page<MemberFitness> findAllByFitnessIdAndStatusIsNot(Long fitnessId, Pageable pageable, Status status);

    List<MemberFitness> findAllByMember(Member member);

    Page<MemberFitness> findAllByMemberIn(Pageable pageable, List<Member> member);

    List<MemberFitness> findByStatusAndActiveTimeBefore(Status status, LocalDateTime time);

    boolean existsByMemberAndStatusIn(Member member, List<Status> status);

    boolean existsByMemberIsAndCreatedAtBetween(Member member, LocalDateTime start, LocalDateTime end);

    int countAllByCreatedAtGreaterThanEqualAndCreatedAtLessThan(LocalDateTime greaterThan, LocalDateTime lessThan);

    int countAllByActiveTimeGreaterThanEqualAndActiveTimeLessThan(LocalDateTime greaterThan, LocalDateTime lessThan);

    List<MemberFitness> findAllByStatusIsAndCreatedAtLessThan(Status status, LocalDateTime createdAt);
}
