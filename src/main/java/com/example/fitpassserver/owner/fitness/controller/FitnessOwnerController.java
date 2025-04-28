package com.example.fitpassserver.owner.fitness.controller;


import com.example.fitpassserver.admin.fitness.dto.response.FitnessAdminResponseDTO;
import com.example.fitpassserver.admin.fitness.service.FitnessAdminService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.owner.fitness.dto.request.FitnessOwnerRequestDTO;
import com.example.fitpassserver.owner.fitness.dto.response.FitnessOwnerResponseDTO;
import com.example.fitpassserver.owner.fitness.service.FitnessOwnerService;
import com.example.fitpassserver.owner.owner.annotation.CurrentOwner;
import com.example.fitpassserver.owner.owner.entity.Owner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/owner/fitness")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "피트니스 사장님 API")
public class FitnessOwnerController {

    private final FitnessOwnerService fitnessOwnerService;

    @Operation(
            summary = "Fitness 생성",
            description = "메인 이미지, 추가 이미지(선택) 및 Fitness 정보를 포함하여 새로운 Fitness를 생성합니다."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Long> createOwnerFitness(
            @Parameter(description = "메인 이미지 파일", required = true)
            @RequestPart("mainImage") MultipartFile mainImage,
            @Parameter(description = "추가 이미지 파일 리스트 (선택)", required = false)
            @RequestPart(value = "additionalImages", required = false) List<MultipartFile> additionalImages,
            @Parameter(description = "Fitness 생성 요청 데이터", required = true)
            @RequestPart FitnessOwnerRequestDTO.FitnessRequestDTO request,
            @CurrentOwner Owner owner
            ) throws IOException
     {
        Long fitnessId = fitnessOwnerService.createFitness(mainImage, additionalImages, request, owner.getLoginId());
        return ApiResponse.onSuccess(fitnessId);
    }

    @Operation(
            summary = "보유 시설 조회",
            description = "로그인한 사업자가 등록한 시설들을 조회합니다."
    )
    @GetMapping
    public ApiResponse<FitnessOwnerResponseDTO.FitnessListDTO> getFitnessList(
            @CurrentOwner Owner owner,
            @Parameter(description = "페이지네이션 커서 값 (첫 페이지는 0)", required = true)
            @RequestParam Long cursor,
            @Parameter(description = "한 페이지당 조회할 시설 수", required = true)
            @RequestParam Integer size) {
        return ApiResponse.onSuccess(fitnessOwnerService.getFitnessList(owner.getId(), cursor, size));
    }

    @Operation(
            summary = "Fitness 수정",
            description = "주어진 fitnessId에 해당하는 Fitness 정보를 업데이트합니다."
    )
    @PutMapping("/{fitnessId}")
    public ApiResponse<FitnessAdminResponseDTO.FitnessInfoDTO> updateFitness(
            @Parameter(description = "수정할 Fitness 정보", required = true)
            @RequestBody FitnessOwnerRequestDTO.FitnessRequestDTO request,
            @Parameter(description = "수정할 Fitness ID", required = true, example = "1")
            @PathVariable Long fitnessId,
            @CurrentOwner Owner owner){
        FitnessAdminResponseDTO.FitnessInfoDTO result = fitnessOwnerService.updateFitness(owner.getId(), fitnessId, request);
        return ApiResponse.onSuccess(result);
    }
}
