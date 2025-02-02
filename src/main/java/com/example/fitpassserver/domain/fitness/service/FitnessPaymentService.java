package com.example.fitpassserver.domain.fitness.service;

import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.fitness.controller.response.FitnessPaymentResponse;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FitnessPaymentService {

    private final FitnessRepository fitnessRepository;
    private final CoinRepository coinRepository;
    private final FitnessImageService fitnessImageService;

    @Transactional(readOnly = true)
    public FitnessPaymentResponse getFitnessPaymentDetail(Long fitnessId, Member member) {
        Fitness fitness = fitnessRepository.findById(fitnessId).orElseThrow(() ->
                new FitnessException(FitnessErrorCode.FITNESS_NOT_FOUND));

        String imageUrl = fitnessImageService.getFitnessImage(fitnessId);

        Long coinQuantity = coinRepository.findAllByMemberAndExpiredDateGreaterThanEqual(member, LocalDate.now())
                .stream()
                .mapToLong(coin -> coin.getCount() != null ? coin.getCount() : 0L).sum();
        return FitnessPaymentResponse.toFitnessPaymentResponse(fitness, coinQuantity, imageUrl);
    }
}
