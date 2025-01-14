package com.example.fitpassserver.domain.review.service;


import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.domain.fitness.repository.MemberFitnessRepository;
import com.example.fitpassserver.domain.review.converter.ReviewConverter;
import com.example.fitpassserver.domain.review.dto.request.ReviewReqDTO;
import com.example.fitpassserver.domain.review.dto.response.ReviewResDTO;
import com.example.fitpassserver.domain.review.entity.Review;
import com.example.fitpassserver.domain.review.exception.ReviewErrorCode;
import com.example.fitpassserver.domain.review.exception.ReviewException;
import com.example.fitpassserver.domain.review.repsotiory.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberFitnessRepository memberFitnessRepository;

    public Long createReview(Long passId ,ReviewReqDTO.CreateReviewReqDTO dto){

        if(!dto.isAgree()){
            throw new ReviewException(ReviewErrorCode.REVIEW_POLICY_NOT_AGREED);
        }

        MemberFitness pass = memberFitnessRepository.findById(passId).orElseThrow(() -> new FitnessException(FitnessErrorCode.PASS_NOT_FOUND));
        if(!pass.getStatus().equals(Status.DONE)){
            throw new FitnessException(FitnessErrorCode.INVALID_PASS);
        }

        Review review = ReviewConverter.toEntity(dto);
        review.setMemberFitness(pass);
        review.setFitness(pass.getFitness());
        reviewRepository.save(review);

        return review.getId();
    }

    @Transactional(readOnly = true)
    public ReviewResDTO.ReviewPageDto getReviewsByFitnessId(Long fitnessId, int page, int size, String sortBy) {
        Sort sort;

        // 정렬 기준에 따라 Sort 객체 설정
        if ("score".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Order.desc("score")); // 별점순
        } else {
            sort = Sort.by(Sort.Order.desc("createdAt")); // 최신순 (기본값)
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return ReviewConverter.toPageDto(reviewRepository.findByFitnessId(fitnessId, pageable));
    }

    public void updateReview(Long reviewId, ReviewReqDTO.UpdateReviewReqDTO dto){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
        review.update(dto.getContent(), dto.getScore());
    }

    public void deleteReview(Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
    }

}
