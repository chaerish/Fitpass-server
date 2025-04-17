package com.example.fitpassserver.admin.owner.service.query;


import com.example.fitpassserver.admin.owner.dto.OwnerAdminResponseDTO;

public interface OwnerAdminQueryService {
    OwnerAdminResponseDTO.OwnerPagesDTO getOwnersInfo(int page, int size, String searchType, String keyword);

    OwnerAdminResponseDTO.OwnerApprovalPagesDTO getOwnersApproval(int page, int size, String searchType, String keyword);

    OwnerAdminResponseDTO.OwnerGetPresignedUrlDTO getFile(String loginId, String name);
}
