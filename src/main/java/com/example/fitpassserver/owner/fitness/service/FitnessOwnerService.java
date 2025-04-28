package com.example.fitpassserver.owner.fitness.service;


import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.admin.fitness.dto.response.FitnessAdminResponseDTO;
import com.example.fitpassserver.owner.fitness.dto.request.FitnessOwnerRequestDTO;
import com.example.fitpassserver.owner.fitness.dto.response.FitnessOwnerResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FitnessOwnerService {
    Long createFitness(MultipartFile mainImage, List<MultipartFile> additionalImages, FitnessOwnerRequestDTO.FitnessRequestDTO dto, String loginId) throws IOException;
    FitnessOwnerResponseDTO.FitnessListDTO getFitnessList(Long memberId, Long cursor, int size);
    FitnessAdminResponseDTO.FitnessInfoDTO updateFitness(Long ownerId, Long fitnessId, FitnessOwnerRequestDTO.FitnessRequestDTO dto);
}
