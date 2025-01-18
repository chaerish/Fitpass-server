package com.example.fitpassserver.domain.review.controller;

import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.review.dto.request.ReviewReqDTO;
import com.example.fitpassserver.domain.review.dto.response.ReviewResDTO;
import com.example.fitpassserver.domain.review.service.ReviewService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/fitness")
@Tag(name = "리뷰 API")
public class ReviewController {
    private final ReviewService reviewService;


    @Operation(
            summary = "리뷰 생성",
            description = "지정한 passId에 대한 리뷰를 작성합니다."
    )
    @PostMapping("/{passId}/review")
    public ApiResponse<?> createReview(
            @Parameter(description = "현재 인증된 사용자 정보", hidden = true) @CurrentMember Member member,
            @Parameter(description = "리뷰를 작성할 패스 ID", example = "1") @PathVariable Long passId,
            @RequestBody ReviewReqDTO.CreateReviewReqDTO dto) {
        Long result = reviewService.createReview(member, passId, dto);
        return ApiResponse.onSuccess(Map.of("id", result));
    }

    @Operation(
            summary = "리뷰 목록 조회",
            description = "특정 피트니스 ID에 대한 리뷰 목록을 조회합니다."
    )
    @GetMapping("/{fitnessId}/review")
    public ApiResponse<ReviewResDTO.ReviewPageDto> getReviewPage(
            @Parameter(description = "현재 인증된 사용자 정보", hidden = true) @CurrentMember Member member,
            @Parameter(description = "페이지 시작 오프셋 (기본값: 0)", example = "0") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "페이지 크기 (기본값: 5)", example = "5") @RequestParam(defaultValue = "5") int pageSize,
            @Parameter(description = "정렬 기준 (기본값: date)", example = "date") @RequestParam(defaultValue = "date") String sortBy,
            @Parameter(description = "조회할 피트니스 ID", example = "2") @PathVariable Long fitnessId) {
        ReviewResDTO.ReviewPageDto reviews = reviewService.getReviewsByFitnessId(member, fitnessId, offset, pageSize, sortBy);
        return ApiResponse.onSuccess(reviews);
    }

    @Operation(
            summary = "리뷰 수정",
            description = "지정한 reviewId에 대한 리뷰를 수정합니다."
    )
    @PatchMapping("/review/{reviewId}")
    public ApiResponse<String> updateReview(
            @Parameter(description = "현재 인증된 사용자 정보", hidden = true) @CurrentMember Member member,
            @Parameter(description = "수정할 리뷰 ID", example = "3") @PathVariable Long reviewId,
            @RequestBody ReviewReqDTO.UpdateReviewReqDTO dto) {
        reviewService.updateReview(member, reviewId, dto);
        return ApiResponse.onSuccess("해당 리뷰가 수정되었습니다.");
    }

    @Operation(
            summary = "리뷰 삭제",
            description = "지정한 reviewId에 대한 리뷰를 삭제합니다."
    )
    @DeleteMapping("/review/{reviewId}")
    public ApiResponse<String> deleteReview(
            @Parameter(description = "현재 인증된 사용자 정보", hidden = true) @CurrentMember Member member,
            @Parameter(description = "삭제할 리뷰 ID", example = "4") @PathVariable Long reviewId) {
        reviewService.deleteReview(member, reviewId);
        return ApiResponse.onSuccess("해당 리뷰가 삭제되었습니다.");
    }

}
