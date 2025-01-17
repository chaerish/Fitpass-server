package com.example.fitpassserver.domain.review.converter;

import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.review.dto.request.ReviewReqDTO;
import com.example.fitpassserver.domain.review.dto.response.ReviewResDTO;
import com.example.fitpassserver.domain.review.entity.Review;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewConverter {
    public static Review toEntity(ReviewReqDTO.CreateReviewReqDTO dto) {
        return Review.builder()
                .content(dto.getContent())
                .score(dto.getScore())
                .isAgree(dto.isAgree())
                .build();
    }


    public static ReviewResDTO.ReviewPageDto toPageDto(Member member, Page<Review> reviewPage) {
        List<ReviewResDTO.ReviewPreviewResDTO> reviews = reviewPage.getContent().stream()
                .map(review -> ReviewResDTO.ReviewPreviewResDTO.builder()
                        .id(review.getId())
                        .content(review.getContent())
                        .score(review.getScore())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        .isOwner(review.getMemberFitness().getMember().getId().equals(member.getId()))
                        .build())
                .collect(Collectors.toList());

        return ReviewResDTO.ReviewPageDto.builder()
                .reviews(reviews)
                .totalPages(reviewPage.getTotalPages())
                .totalElements(reviewPage.getTotalElements())
                .build();
    }
}
