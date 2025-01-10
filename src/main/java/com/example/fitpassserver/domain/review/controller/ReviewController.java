package com.example.fitpassserver.domain.review.controller;

import com.example.fitpassserver.domain.review.dto.request.ReviewReqDTO;
import com.example.fitpassserver.domain.review.dto.response.ReviewResDTO;
import com.example.fitpassserver.domain.review.entity.Review;
import com.example.fitpassserver.domain.review.service.ReviewService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/fitness")
@Tag(name = "리뷰 API")
public class ReviewController {
    private final ReviewService reviewService;


    @PostMapping("/{passId}/review")
    public ApiResponse<?> createReview(@PathVariable Long passId, @RequestBody ReviewReqDTO.CreateReviewReqDTO dto){
        Long result = reviewService.createReview(passId, dto);
        return ApiResponse.onSuccess(Map.of("id", result));
    }

    @GetMapping("/{fitnessId}/review")
    public ApiResponse<ReviewResDTO.ReviewPageDto> getReviewPage(@RequestParam(defaultValue = "0") int offset,
                                        @RequestParam(defaultValue = "5") int pageSize,
                                        @RequestParam(defaultValue = "date") String sortBy,
                                        @PathVariable Long fitnessId
                                        ){
        ReviewResDTO.ReviewPageDto reviews = reviewService.getReviewsByFitnessId(fitnessId, offset, pageSize, sortBy);
        return ApiResponse.onSuccess(reviews);
    }

    @PatchMapping("/review/{reviewId}")
    public ApiResponse<String> updateReview(@PathVariable Long reviewId, @RequestBody ReviewReqDTO.UpdateReviewReqDTO dto){
        reviewService.updateReview(reviewId, dto);
        return ApiResponse.onSuccess("해당 리뷰가 수정되었습니다.");
    }

    @DeleteMapping("/review/{reviewId}")
    public ApiResponse<String> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return ApiResponse.onSuccess("해당 리뷰가 삭제되었습니다.");
    }
}
