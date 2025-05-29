package com.example.fitpassserver.admin.fitness.service;

import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.admin.fitness.dto.response.FitnessAdminResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FitnessAdminService {
    Long createFitness(MultipartFile mainImage, List<MultipartFile> additionalImages, FitnessAdminRequestDTO.FitnessReqDTO dto) throws IOException;
    FitnessAdminResponseDTO.FitnessListDTO getFitnessList(int page, int size, String searchType, String keyword);
    FitnessAdminResponseDTO.FitnessAdminPreviewDTO getFitness(Long fitnessId);
    FitnessAdminResponseDTO.FitnessInfoDTO updateFitness(Long fitnessId, FitnessAdminRequestDTO.FitnessReqDTO dto);
    void deleteFitness(Long fitnessId);
    FitnessAdminResponseDTO.FitnessInfoDTO updatePurchaseStatus(Long fitnessId);
}
