package com.example.fitpassserver.admin.fitness.controller;

import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.admin.fitness.dto.response.FitnessAdminResponseDTO;
import com.example.fitpassserver.admin.fitness.service.FitnessAdminService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.Path;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/fitness")
@Tag(name = "피트니스 어드민 API")
public class FitnessAdminController {

    private final FitnessAdminService fitnessAdminService;


    @Operation(
            summary = "Fitness 생성",
            description = "메인 이미지, 추가 이미지(선택) 및 Fitness 정보를 포함하여 새로운 Fitness를 생성합니다."
    )
    @PostMapping(value = "/createFitness", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Long> createFitness(
            @Parameter(description = "메인 이미지 파일", required = true)
            @RequestPart("mainImage") MultipartFile mainImage,
            @Parameter(description = "추가 이미지 파일 리스트 (선택)", required = false)
            @RequestPart(value = "additionalImages", required = false) List<MultipartFile> additionalImages,
            @Parameter(description = "Fitness 생성 요청 데이터", required = true)
            @RequestPart FitnessAdminRequestDTO.FitnessReqDTO request) throws IOException {

        Long fitnessId = fitnessAdminService.createFitness(mainImage, additionalImages, request);
        return ApiResponse.onSuccess(fitnessId);
    }

    @Operation(
            summary = "Fitness 목록 조회",
            description = "페이지네이션, 정렬, 검색 기능을 포함한 Fitness 목록을 조회합니다."
    )
    @GetMapping
    public ApiResponse<FitnessAdminResponseDTO.FitnessListDTO> getFitnessList(
            @Parameter(description = "페이지 번호 (기본값: 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기 (기본값: 10)", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "검색 유형 (예: name, category, phoneNumber)", example = "name")
            @RequestParam(required = false) String searchType, // 시설명, 카테고리, 전화번호 중 하나 선택
            @Parameter(description = "검색 키워드", example = "피트니스")
            @RequestParam(required = false) String keyword // 검색어
    ){

        FitnessAdminResponseDTO.FitnessListDTO result = fitnessAdminService.getFitnessList(page, size, searchType, keyword);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "Fitness 수정",
            description = "주어진 fitnessId에 해당하는 Fitness 정보를 업데이트합니다."
    )
    @PutMapping("/{fitnessId}")
    public ApiResponse<FitnessAdminResponseDTO.FitnessInfoDTO> updateFitness(
            @Parameter(description = "수정할 Fitness 정보", required = true)
            @RequestBody FitnessAdminRequestDTO.FitnessReqDTO request,
            @Parameter(description = "수정할 Fitness ID", required = true, example = "1")
            @PathVariable Long fitnessId){
        FitnessAdminResponseDTO.FitnessInfoDTO result = fitnessAdminService.updateFitness(fitnessId, request);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "Fitness 삭제",
            description = "주어진 fitnessId에 해당하는 Fitness 정보를 삭제합니다."
    )
    @DeleteMapping("/{fitnessId}")
    public ApiResponse<String> deleteFitness(
            @Parameter(description = "삭제할 Fitness ID", required = true, example = "1")
            @PathVariable Long fitnessId){
        fitnessAdminService.deleteFitness(fitnessId);
        return ApiResponse.onSuccess("시설 삭제가 완료되었습니다.");
    }
}
