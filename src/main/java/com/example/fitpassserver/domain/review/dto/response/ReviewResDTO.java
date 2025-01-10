package com.example.fitpassserver.domain.review.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewPreviewResDTO {
        public Long id;
        public String content;
        public double score;
        public LocalDateTime createdAt;
        public LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewPageDto {
        private List<ReviewPreviewResDTO> reviews;
        private int totalPages;
        private long totalElements;
    }
}
