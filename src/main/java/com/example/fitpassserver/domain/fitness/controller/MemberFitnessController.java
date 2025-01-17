package com.example.fitpassserver.domain.fitness.controller;


import com.example.fitpassserver.domain.fitness.dto.response.MemberFitnessResDTO;
import com.example.fitpassserver.domain.fitness.service.MemberFitnessService;
import com.example.fitpassserver.domain.fitness.converter.MemberFitnessConverter;
import com.example.fitpassserver.domain.fitness.dto.MemberFitnessRequestDTO;
import com.example.fitpassserver.domain.fitness.dto.MemberFitnessResponseDTO;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.service.command.MemberFitnessCommandService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/fitness/pass")
@Tag(name = "패스 API")
public class MemberFitnessController {

    private final MemberFitnessService memberFitnessService;
    private final MemberFitnessCommandService memberFitnessCommandService;

    @PostMapping
    @Operation(summary = "피트니스 패스 구매 API", description = "피트니스 패스 구매 시 사용하는 API")
    public ApiResponse<MemberFitnessResponseDTO.CreateMemberFitnessResponseDTO> createMemberFitness(@CurrentMember Member member, @RequestBody MemberFitnessRequestDTO.CreateMemberFitnessRequestDTO dto) {
        MemberFitness memberFitness = memberFitnessCommandService.buyFitness(member, dto);
        return ApiResponse.onSuccess(MemberFitnessConverter.toCreateMemberFitnessResponseDTO(memberFitness));
    }

    @PostMapping("/cancel")
    @Operation(summary = "피트니스 패스 구매 취소 API", description = "피트니스 패스 구매 취소 시 사용하는 API")
    public ApiResponse<MemberFitnessResponseDTO.CancelMemberFitnessResponseDTO> cancelPass(@CurrentMember Member member, @RequestBody MemberFitnessRequestDTO.CancelMemberFitnessRequestDTO dto) {
        MemberFitness memberFitness = memberFitnessCommandService.cancelFitness(member, dto);
        return ApiResponse.onSuccess(MemberFitnessConverter.toCancelMemberFitnessResponseDTO(memberFitness));
    }

    @Operation(
            summary = "보유 패스 조회",
            description = "사용자의 보유 패스 정보를 조회합니다. 패스 상태는 3가지(NONE, PROGRESS, DONE)로 나눠서 조회됩니다."
    )
    @GetMapping
    public ApiResponse<MemberFitnessResDTO.MemberFitnessGroupDTO> getPassList(
            @Parameter(description = "현재 인증된 사용자 정보", hidden = true)
            @CurrentMember Member member) {
        MemberFitnessResDTO.MemberFitnessGroupDTO result = memberFitnessService.getPassList(member.getLoginId());
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "패스 사용",
            description = "지정한 passId의 패스를 사용합니다. 사용자가 약관 동의 여부를 함께 전달해야 합니다."
    )
    @PatchMapping("/{passId}")
    public ApiResponse<String> usePass(
            @Parameter(description = "현재 인증된 사용자 정보", hidden = true)
            @CurrentMember Member member,
            @Parameter(description = "사용할 패스 ID", example = "1")
            @PathVariable Long passId,
            @Parameter(description = "약관 동의 여부", example = "true")
            @RequestParam boolean isAgree) {
        memberFitnessService.usePass(member, passId, isAgree);
        return ApiResponse.onSuccess("해당 패스를 사용합니다.");
    }

    @Operation(
            summary = "패스 상세 조회",
            description = "지정한 passId에 대한 상세 정보를 조회합니다."
    )
    @GetMapping("/{passId}")
    public ApiResponse<MemberFitnessResDTO.MemberFitnessPreviewDTO> getPass(
            @Parameter(description = "현재 인증된 사용자 정보", hidden = true)
            @CurrentMember Member member,
            @Parameter(description = "조회할 패스 ID", example = "1")
            @PathVariable Long passId) {
        MemberFitnessResDTO.MemberFitnessPreviewDTO result = memberFitnessService.getPass(member, passId);
        return ApiResponse.onSuccess(result);
    }
}
