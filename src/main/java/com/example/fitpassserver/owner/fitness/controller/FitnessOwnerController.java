package com.example.fitpassserver.owner.fitness.controller;


import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.admin.fitness.service.FitnessAdminService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.owner.fitness.service.FitnessOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/owenr/fitness")
@RequiredArgsConstructor
@Tag(name = "피트니스 Owner API")
public class FitnessOwnerController {

    private final FitnessAdminService fitnessAdminService;
    private final FitnessOwnerService fitnessOwnerService;

    @Operation(
            summary = "Fitness 생성",
            description = "메인 이미지, 추가 이미지(선택) 및 Fitness 정보를 포함하여 새로운 Fitness를 생성합니다."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Long> createFitness(
            @Parameter(description = "메인 이미지 파일", required = true)
            @RequestPart("mainImage") MultipartFile mainImage,
            @Parameter(description = "추가 이미지 파일 리스트 (선택)", required = false)
            @RequestPart(value = "additionalImages", required = false) List<MultipartFile> additionalImages,
            @Parameter(description = "Fitness 생성 요청 데이터", required = true)
            @RequestPart FitnessAdminRequestDTO.FitnessReqDTO request,
            @CurrentMember Member member
            ) throws IOException
     {
        Long fitnessId = fitnessOwnerService.createFitness(mainImage, additionalImages, request, member.getId());
        return ApiResponse.onSuccess(fitnessId);
    }

    @GetMapping
    public ApiResponse<?> getFitnessList(){
        return null;
    }
}
