package com.example.fitpassserver.admin.management.service;

import com.example.fitpassserver.admin.management.dto.request.ManagementAdminRequestDTO;
import com.example.fitpassserver.admin.management.dto.response.ManagementAdminResponseDTO;
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

    @Transactional
    public void updateAllPlans(List<ManagementAdminRequestDTO.UpdateCoinManagementDTO> requestList) {
        for (ManagementAdminRequestDTO.UpdateCoinManagementDTO request : requestList) {
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
        List<PlanTypeEntity> plans = planTypeRepository.findAll();
        return plans.stream()
                .map(plan -> new ManagementAdminResponseDTO.PlanInfoDTO(plan))
                .collect(Collectors.toList());
    }
}
