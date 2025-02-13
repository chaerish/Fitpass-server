package com.example.fitpassserver.admin.management.service;

import com.example.fitpassserver.admin.management.dto.request.ManagementAdminRequestDTO;
import com.example.fitpassserver.admin.management.dto.response.ManagementAdminResponseDTO;

import java.util.List;

public interface ManagementAdminService {
    void updateAllPlans(List<ManagementAdminRequestDTO.UpdateCoinManagementDTO> requestList);

    List<ManagementAdminResponseDTO.PlanInfoDTO> getAllPlans();
}
