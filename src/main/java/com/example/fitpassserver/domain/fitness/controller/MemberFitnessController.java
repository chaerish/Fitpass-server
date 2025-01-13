package com.example.fitpassserver.domain.fitness.controller;

import com.example.fitpassserver.domain.fitness.converter.MemberFitnessConverter;
import com.example.fitpassserver.domain.fitness.dto.MemberFitnessRequestDTO;
import com.example.fitpassserver.domain.fitness.dto.MemberFitnessResponseDTO;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.service.command.MemberFitnessCommandService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pass")
public class MemberFitnessController {

    private final MemberFitnessCommandService memberFitnessCommandService;

    @PostMapping
    @Operation(summary = "피트니스 패스 구매 API", description = "피트니스 패스 구매 시 사용하는 API")
    public ApiResponse<MemberFitnessResponseDTO.CreateMemberFitnessResponseDTO> createMemberFitness(@CurrentMember Member member, @RequestBody MemberFitnessRequestDTO.CreateMemberFitnessRequestDTO dto) {
        MemberFitness memberFitness = memberFitnessCommandService.buyFitness(member, dto);
        return ApiResponse.onSuccess(MemberFitnessConverter.toCreateMemberFitnessResponseDTO(memberFitness));
    }

}
