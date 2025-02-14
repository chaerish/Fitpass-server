package com.example.fitpassserver.admin.management.service;

import com.example.fitpassserver.admin.management.dto.request.ManagementAdminRequestDTO;
import com.example.fitpassserver.admin.management.dto.response.ManagementAdminResponseDTO;
import com.example.fitpassserver.domain.coin.entity.CoinTypeEntity;
import com.example.fitpassserver.domain.coin.exception.CoinErrorCode;
import com.example.fitpassserver.domain.coin.exception.CoinException;
import com.example.fitpassserver.domain.coin.repository.CoinTypeRepository;
import com.example.fitpassserver.domain.plan.entity.PlanTypeEntity;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagementAdminServiceImpl implements ManagementAdminService {
    private final PlanTypeRepository planTypeRepository;
    private final CoinTypeRepository coinTypeRepository;

    /**
     * 구독 관리
     **/
    @Transactional
    public void updateAllPlans(List<ManagementAdminRequestDTO.UpdatePlanManagementDTO> requestList) {
        for (ManagementAdminRequestDTO.UpdatePlanManagementDTO request : requestList) {
            PlanTypeEntity planTypeEntity = planTypeRepository.findByPlanType(request.getPlanType())
                    .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_PAYMENT_BAD_REQUEST));

            planTypeEntity.updatePlan(
                    request.getPlanType(),
                    request.getPrice(),
                    request.getCoinQuantity(),
                    request.getCoinAddition(),
                    request.getExpirationPeriod()
            );
        }
    }

    @Override
    public List<ManagementAdminResponseDTO.PlanInfoDTO> getAllPlans() {
        List<PlanTypeEntity> plans = planTypeRepository.findAllSortedByCoinQuantity();
        return plans.stream()
                .map(plan -> new ManagementAdminResponseDTO.PlanInfoDTO(plan))
                .collect(Collectors.toList());
    }

    /**
     * 코인 관리
     **/
    @Transactional
    public void updateAllCoins(List<ManagementAdminRequestDTO.UpdateCoinManagementDTO> requestList) {
        for (ManagementAdminRequestDTO.UpdateCoinManagementDTO request : requestList) {
            CoinTypeEntity coinTypeEntity = coinTypeRepository.findByName(request.getName())
                    .orElseThrow(() -> new CoinException(CoinErrorCode.COIN_NOT_FOUND));

            coinTypeEntity.updateCoinType(
                    request.getName(),
                    request.getPrice(),
                    request.getCoinQuantity(),
                    request.getCoinAddition(),
                    request.getExpirationPeriod()
            );
        }
    }

    @Override
    public List<ManagementAdminResponseDTO.CoinInfoDTO> getAllCoins() {
        List<CoinTypeEntity> coins = coinTypeRepository.findAllSortedByCoinQuantity();
        return coins.stream()
                .map(coin -> new ManagementAdminResponseDTO.CoinInfoDTO(coin))
                .collect(Collectors.toList());
    }
}
