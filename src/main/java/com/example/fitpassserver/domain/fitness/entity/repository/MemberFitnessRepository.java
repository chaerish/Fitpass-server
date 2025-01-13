package com.example.fitpassserver.domain.fitness.entity.repository;

import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberFitnessRepository extends JpaRepository<MemberFitness, Long> {
    Optional<MemberFitness> findByMemberIsAndFitnessIs(Member member, Fitness fitness);
}
