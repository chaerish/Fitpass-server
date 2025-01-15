package com.example.fitpassserver.domain.fitness.repository;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberFitnessRepository extends JpaRepository<MemberFitness, Long> {
    List<MemberFitness> findAllByMember(Member member);
    Optional<MemberFitness> findByMemberIsAndFitnessIs(Member member, Fitness fitness);
}
