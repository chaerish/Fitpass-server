package com.example.fitpassserver.domain.fitness.converter;

import com.example.fitpassserver.domain.fitness.dto.MemberFitnessRequestDTO;
import com.example.fitpassserver.domain.fitness.dto.MemberFitnessResponseDTO;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.fitness.dto.response.MemberFitnessResDTO;

import java.util.ArrayList;
import java.util.List;

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

    public static MemberFitnessResDTO.MemberFitnessPreviewDTO toDto(MemberFitness memberFitness) {
        return MemberFitnessResDTO.MemberFitnessPreviewDTO.builder()
                .id(memberFitness.getId())
                .status(memberFitness.getStatus())
                .activeTime(memberFitness.getActiveTime())
                .memberId(memberFitness.getMember().getId())
                .fitnessId(memberFitness.getFitness().getId())
                .build();
    }

    public static MemberFitnessResponseDTO.CreateMemberFitnessResponseDTO toCreateMemberFitnessResponseDTO(MemberFitness memberFitness) {
        return MemberFitnessResponseDTO.CreateMemberFitnessResponseDTO.builder()
                .memberFitnessId(memberFitness.getId())
                .build();
    }

    public static MemberFitnessResDTO.MemberFitnessGroupDTO toGroupDto(List<MemberFitness> memberFitnessList) {
        List<MemberFitnessResDTO.MemberFitnessPreviewDTO> none = new ArrayList<>();
        List<MemberFitnessResDTO.MemberFitnessPreviewDTO> progress = new ArrayList<>();
        List<MemberFitnessResDTO.MemberFitnessPreviewDTO> done = new ArrayList<>();

        for (MemberFitness memberFitness : memberFitnessList) {
            MemberFitnessResDTO.MemberFitnessPreviewDTO dto = toDto(memberFitness);
            switch (memberFitness.getStatus()) {
                case NONE:
                    none.add(dto);
                    break;
                case PROGRESS:
                    progress.add(dto);
                    break;
                case DONE:
                    done.add(dto);
                    break;
            }
        }

        return MemberFitnessResDTO.MemberFitnessGroupDTO.builder()
                .none(none)
                .progress(progress)
                .done(done)
                .build();
    }
}
