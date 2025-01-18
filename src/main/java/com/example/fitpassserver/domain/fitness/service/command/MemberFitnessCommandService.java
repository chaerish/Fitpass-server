package com.example.fitpassserver.domain.fitness.service.command;

import com.example.fitpassserver.domain.fitness.dto.MemberFitnessRequestDTO;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.member.entity.Member;

public interface MemberFitnessCommandService {
    MemberFitness buyFitness(Member member, MemberFitnessRequestDTO.CreateMemberFitnessRequestDTO dto);
    MemberFitness cancelFitness(Member member, MemberFitnessRequestDTO.CancelMemberFitnessRequestDTO dto);
}
