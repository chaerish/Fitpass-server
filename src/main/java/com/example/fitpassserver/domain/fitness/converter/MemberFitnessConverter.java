package com.example.fitpassserver.domain.fitness.converter;

import com.example.fitpassserver.domain.fitness.dto.MemberFitnessRequestDTO;
import com.example.fitpassserver.domain.fitness.dto.MemberFitnessResponseDTO;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.member.entity.Member;

import java.time.LocalDateTime;

public class MemberFitnessConverter {

    public static MemberFitness toEntity(Member member, Fitness fitness, MemberFitnessRequestDTO.CreateMemberFitnessRequestDTO dto) {
        return MemberFitness.builder()
                .status(Status.NONE)
                .activeTime(null)
                .isAgree(dto.isAgree())
                .member(member)
                .fitness(fitness)
                .build();
    }

    public static MemberFitnessResponseDTO.CreateMemberFitnessResponseDTO toCreateMemberFitnessResponseDTO(MemberFitness memberFitness) {
        return MemberFitnessResponseDTO.CreateMemberFitnessResponseDTO.builder()
                .memberFitnessId(memberFitness.getId())
                .build();
    }
}
