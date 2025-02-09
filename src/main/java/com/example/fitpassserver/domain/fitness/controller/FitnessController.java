package com.example.fitpassserver.domain.fitness.controller;

import com.example.fitpassserver.domain.fitness.controller.response.CursorPaginationResponse;
import com.example.fitpassserver.domain.fitness.controller.response.FitnessDetailResponse;
import com.example.fitpassserver.domain.fitness.controller.response.FitnessListResponse;
import com.example.fitpassserver.domain.fitness.controller.response.FitnessPaymentResponse;
import com.example.fitpassserver.domain.fitness.controller.response.FitnessRecommendResponse;
import com.example.fitpassserver.domain.fitness.controller.response.FitnessSearchResponse;
import com.example.fitpassserver.domain.fitness.service.FitnessDetailService;
import com.example.fitpassserver.domain.fitness.service.FitnessListService;
import com.example.fitpassserver.domain.fitness.service.FitnessPaymentService;
import com.example.fitpassserver.domain.fitness.service.FitnessRecommendService;
import com.example.fitpassserver.domain.fitness.service.FitnessSearchService;
import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fitness")
public class FitnessController {
    private final FitnessRecommendService fitnessRecommendService;
    private final FitnessSearchService fitnessSearchService;
    private final FitnessDetailService fitnessDetailService;
    private final FitnessListService fitnessListService;
    private final FitnessPaymentService fitnessPaymentService;

    private static final double DEFAULT_LATITUDE = 37.5665;
    private static final double DEFAULT_LONGITUDE = 126.9780;


    public FitnessController(FitnessRecommendService fitnessRecommendService, FitnessSearchService fitnessSearchService,
                             FitnessDetailService fitnessDetailService, FitnessListService fitnessListService,
                             FitnessPaymentService fitnessPaymentService) {
        this.fitnessRecommendService = fitnessRecommendService;
        this.fitnessSearchService = fitnessSearchService;
        this.fitnessDetailService = fitnessDetailService;
        this.fitnessListService = fitnessListService;
        this.fitnessPaymentService = fitnessPaymentService;
    }

    @Operation(summary = "메인 페이지에서 필터로 시설 검색할 수 있는 api", description = "무한스크롤이며 카테고리, 정렬 기준으로 정렬합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<CursorPaginationResponse<FitnessListResponse>>> getFitnessList(
            @RequestParam String category,
            @RequestParam(required = false, defaultValue = "distance") String sort,
            // 정렬 기준 (distance, lowPrice, highPrice)
            @RequestParam(required = false) Long cursor, // 커서 값 (Optional, 처음 요청 시 null)
            @RequestParam(defaultValue = "10") int size,
            @CurrentMember Member member
    ) {

        double latitude = (member != null && member.getLatitude() != null) ? member.getLatitude() : DEFAULT_LATITUDE;
        double longitude =
                (member != null && member.getLongitude() != null) ? member.getLongitude() : DEFAULT_LONGITUDE;

        CursorPaginationResponse<FitnessListResponse> result = fitnessListService.getFitnessList(
                category, sort, latitude, longitude, cursor, size
        );
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }

    @Operation(summary = "메인 페이지에서 추천시설 뜨는 api", description = "관리자가 추천 시설로 등록해놓은 시설을 반환합니다.")
    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<List<FitnessRecommendResponse>>> recommendFitness(
            @CurrentMember Member member) {

        double latitude = (member != null && member.getLatitude() != null) ? member.getLatitude() : DEFAULT_LATITUDE;
        double longitude =
                (member != null && member.getLongitude() != null) ? member.getLongitude() : DEFAULT_LONGITUDE;

        List<FitnessRecommendResponse> result = fitnessRecommendService.getRecommendFitnessAsResponse(latitude,
                longitude);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }


    @Operation(summary = "키워드로 시설 검색 api", description = "사용자가 입력한 키워드가 포함된 시설을 반환합니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<CursorPaginationResponse<FitnessSearchResponse>>> searchFitness(
            @RequestParam String keyword,
            @RequestParam(required = false) Long cursor, // 커서 값 (Optional, 처음 요청 시 null)
            @RequestParam(defaultValue = "10") int size,
            @CurrentMember Member member
    ) {
        double latitude = (member != null && member.getLatitude() != null) ? member.getLatitude() : DEFAULT_LATITUDE;
        double longitude =
                (member != null && member.getLongitude() != null) ? member.getLongitude() : DEFAULT_LONGITUDE;

        CursorPaginationResponse<FitnessSearchResponse> result = fitnessSearchService.searchFitnessByKeyword(keyword,
                cursor, size, latitude, longitude);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }


    @Operation(summary = "시설 상세조회 api", description = "시설 하나를 클릭했을 때 상세 정보를 조회할 수 있습니다.")
    @GetMapping("/{fitnessId}")
    public ResponseEntity<ApiResponse<FitnessDetailResponse>> getFitnessDetail(
            @PathVariable Long fitnessId,
            @CurrentMember Member member) {

        double latitude = (member != null && member.getLatitude() != null) ? member.getLatitude() : DEFAULT_LATITUDE;
        double longitude =
                (member != null && member.getLongitude() != null) ? member.getLongitude() : DEFAULT_LONGITUDE;

        FitnessDetailResponse response = fitnessDetailService.getFitnessDetail(fitnessId, latitude, longitude);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "피트니스 구매 정보 API", description = "피트니스 구매 전의 정보를 조회할 수 있습니다.")
    @GetMapping("/payment/{fitnessId}")
    public ApiResponse<FitnessPaymentResponse> getFitnessPaymentDetail(@CurrentMember Member member,
                                                                       @PathVariable Long fitnessId) {
        return ApiResponse.onSuccess(fitnessPaymentService.getFitnessPaymentDetail(fitnessId, member));
    }


}
