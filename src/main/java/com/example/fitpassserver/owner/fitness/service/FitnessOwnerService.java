package com.example.fitpassserver.owner.fitness.service;


import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FitnessOwnerService {
    Long createFitness(MultipartFile mainImage, List<MultipartFile> additionalImages, FitnessAdminRequestDTO.FitnessReqDTO dto, Long memberId) throws IOException;
}
