package com.example.fitpassserver.domain.fitness.controller;


import com.example.fitpassserver.domain.fitness.dto.response.MemberFitnessResDTO;
import com.example.fitpassserver.domain.fitness.service.MemberFitnessService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping
    public ApiResponse<MemberFitnessResDTO.MemberFitnessGroupDTO> getPassList(
            @CurrentMember Member member){
        MemberFitnessResDTO.MemberFitnessGroupDTO result = memberFitnessService.getPassList(member.getLoginId());
        return ApiResponse.onSuccess(result);
    }

    @PatchMapping("/{passId}")
    public ApiResponse<String> usePass(@CurrentMember Member member, @PathVariable Long passId, @RequestParam boolean isAgree){
        memberFitnessService.usePass(member, passId, isAgree);
        return ApiResponse.onSuccess("해당 패스를 사용합니다.");
    }

    @GetMapping("/{passId}")
    public ApiResponse<MemberFitnessResDTO.MemberFitnessPreviewDTO> getPass(@CurrentMember Member member, @PathVariable Long passId){
        MemberFitnessResDTO.MemberFitnessPreviewDTO result = memberFitnessService.getPass(member, passId);
        return ApiResponse.onSuccess(result);
    }
}
