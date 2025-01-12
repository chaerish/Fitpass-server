package com.example.fitpassserver.domain.fitness.Controller;

import com.example.fitpassserver.domain.fitness.Controller.response.FitnessDetailResponse;
import com.example.fitpassserver.domain.fitness.Controller.response.FitnessListResponse;
import com.example.fitpassserver.domain.fitness.Controller.response.FitnessRecommendResponse;
import com.example.fitpassserver.domain.fitness.Controller.response.FitnessSearchResponse;
import com.example.fitpassserver.domain.fitness.Service.FitnessDetailService;
import com.example.fitpassserver.domain.fitness.Service.FitnessListService;
import com.example.fitpassserver.domain.fitness.Service.FitnessRecommendService;
import com.example.fitpassserver.domain.fitness.Service.FitnessSearchService;
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
    public ResponseEntity<ApiResponse<List<FitnessListResponse>>> getFitnessList(
            @RequestParam String category,
            @RequestParam String sort,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<FitnessListResponse> result = fitnessListService.getFitnessList(category, sort, latitude, longitude, page, size);
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
