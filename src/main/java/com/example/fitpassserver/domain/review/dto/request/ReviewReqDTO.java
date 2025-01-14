package com.example.fitpassserver.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

public class ReviewReqDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateReviewReqDTO {
        @Schema(description = "리뷰 내용", example = "좋은 서비스였습니다!")
        private String content;

        @Schema(description = "리뷰 점수", example = "4.5")
        private double score;

        @Schema(description = "개인정보 약관 동의 여부", example = "true")
        private boolean isAgree;

        @Schema(description = "패스 ID", example = "1")
        private Long memberFitnessId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateReviewReqDTO {
        @Schema(description = "리뷰 내용", example = "서비스가 아주 훌륭했습니다!")
        private String content;

        @Schema(description = "리뷰 점수", example = "5.0")
        private double score;
    }
}
