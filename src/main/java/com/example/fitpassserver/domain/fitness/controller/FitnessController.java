package com.example.fitpassserver.domain.fitness.controller;

import com.example.fitpassserver.domain.fitness.controller.response.*;
import com.example.fitpassserver.domain.fitness.service.FitnessDetailService;
import com.example.fitpassserver.domain.fitness.service.FitnessListService;
import com.example.fitpassserver.domain.fitness.service.FitnessRecommendService;
import com.example.fitpassserver.domain.fitness.service.FitnessSearchService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/fitness")
public class FitnessController {
    private final FitnessRecommendService fitnessRecommendService;
    private final FitnessSearchService fitnessSearchService;
    private final FitnessDetailService fitnessDetailService;
    private final FitnessListService fitnessListService;

    public FitnessController(FitnessRecommendService fitnessRecommendService, FitnessSearchService fitnessSearchService, FitnessDetailService fitnessDetailService, FitnessListService fitnessListService) {
        this.fitnessRecommendService = fitnessRecommendService;
        this.fitnessSearchService = fitnessSearchService;
        this.fitnessDetailService = fitnessDetailService;
        this.fitnessListService = fitnessListService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CursorPaginationResponse<FitnessListResponse>>> getFitnessList(
            @RequestParam String category,
            @RequestParam(required = false, defaultValue = "distance") String sort, // 정렬 기준 (distance, lowPrice, highPrice)
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false) Long cursor, // 커서 값 (Optional, 처음 요청 시 null)
            @RequestParam(defaultValue = "10") int size // 페이지 크기 (기본: 10)
    ) {
        CursorPaginationResponse<FitnessListResponse> result = fitnessListService.getFitnessList(
                category, sort, latitude, longitude, cursor, size
        );
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }

    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<List<FitnessRecommendResponse>>> recommendFitness(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        List<FitnessRecommendResponse> result = fitnessRecommendService.getRecommendFitnessAsResponse(latitude, longitude);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FitnessSearchResponse>>> searchFitness(@RequestParam String keyword,
                                                                                  @RequestParam double latitude,
                                                                                  @RequestParam double longitude) {
        List<FitnessSearchResponse> result = fitnessSearchService.searchFitnessByKeyword(keyword,latitude,longitude);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }


    @GetMapping("/{fitnessId}")
    public ResponseEntity<ApiResponse<FitnessDetailResponse>> getFitnessDetail(
            @PathVariable Long fitnessId,
            @RequestParam double latitude,
            @RequestParam double longitude) {
        FitnessDetailResponse response = fitnessDetailService.getFitnessDetail(fitnessId, latitude, longitude);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }



}
