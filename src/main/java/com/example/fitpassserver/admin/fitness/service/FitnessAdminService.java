package com.example.fitpassserver.admin.fitness.service;

import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.admin.fitness.dto.response.FitnessAdminResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FitnessAdminService {
    Long createFitness(MultipartFile mainImage, List<MultipartFile> additionalImages, FitnessAdminRequestDTO.CreateFitnessDTO dto) throws IOException;
    public FitnessAdminResponseDTO.FitnessListDTO getFitnessList(int page, int size, String sort, String sortDirection, String searchType, String keyword);
}
