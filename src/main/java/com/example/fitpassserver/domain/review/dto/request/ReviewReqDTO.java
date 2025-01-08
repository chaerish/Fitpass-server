package com.example.fitpassserver.domain.review.dto.request;

import lombok.*;

public class ReviewReqDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateReviewReqDTO {
        private String content;
        private double score;
        private boolean isAgree;
        private Long memberFitnessId;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateReviewReqDTO{
        private String content;
        private double score;
    }
}
