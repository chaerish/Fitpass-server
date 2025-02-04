package com.example.fitpassserver.admin.fitness.controller;

import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.admin.fitness.service.FitnessAdminService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/fitness")
@Tag(name = "피트니스 어드민 API")
public class FitnessAdminController {

    private final FitnessAdminService fitnessAdminService;


    @PostMapping(value = "/createFitness", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Long> createFitness(
            @CurrentMember Member member,
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart(value = "additionalImages", required = false) List<MultipartFile> additionalImages,
            @RequestPart FitnessAdminRequestDTO.CreateFitnessDTO request) throws IOException {

        Long fitnessId = fitnessAdminService.createFitness(member, mainImage, additionalImages, request);
        return ApiResponse.onSuccess(fitnessId);
    }
}
