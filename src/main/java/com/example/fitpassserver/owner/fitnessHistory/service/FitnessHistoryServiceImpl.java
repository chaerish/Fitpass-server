package com.example.fitpassserver.owner.fitnessHistory.service;

import com.example.fitpassserver.domain.coin.entity.CoinType;
import com.example.fitpassserver.domain.coin.repository.CoinTypeRepository;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.entity.Status;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.domain.fitness.repository.MemberFitnessRepository;
import com.example.fitpassserver.owner.fitnessHistory.converter.FitnessHistoryConverter;
import com.example.fitpassserver.owner.fitnessHistory.dto.RevenueHistoryResponseDTO;
import com.example.fitpassserver.owner.fitnessHistory.dto.UsageHistoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FitnessHistoryServiceImpl implements FitnessHistoryService {
    private final FitnessRepository fitnessRepository;
    private final MemberFitnessRepository memberFitnessRepository;
    private final CoinTypeRepository coinTypeRepository;

    @Override
    public UsageHistoryResponseDTO getFitnessUsageHistory(Long fitnessId, int page, int size) {
        Fitness fitness = fitnessRepository.findFitnessById(fitnessId).orElseThrow(
                () -> new FitnessException(FitnessErrorCode.FITNESS_NOT_FOUND)
        );
        Page<MemberFitness> memberFitnessPages = memberFitnessRepository.findAllByFitnessIdAndStatusIsNot(fitnessId,
                PageRequest.of(page, size), Status.NONE);
        return FitnessHistoryConverter.to(memberFitnessPages);
    }

    @Override
    public RevenueHistoryResponseDTO getFitnessRevenueHistory(Long fitnessId, int page, int size) {
        Fitness fitness = fitnessRepository.findFitnessById(fitnessId).orElseThrow(
                () -> new FitnessException(FitnessErrorCode.FITNESS_NOT_FOUND)
        );
        Page<Object[]> memberFitnessPages = memberFitnessRepository.findMonthlyRevenueSummary(fitnessId,
                PageRequest.of(page, size));
        int coinUnits = fitness.getTotalFee() * coinTypeRepository.findByCoinType(CoinType.COIN_1).get().getPrice();
        return FitnessHistoryConverter.to(memberFitnessPages, coinUnits);
    }
}
