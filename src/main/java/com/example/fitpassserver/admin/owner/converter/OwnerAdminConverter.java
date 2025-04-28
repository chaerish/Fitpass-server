package com.example.fitpassserver.admin.owner.converter;

import com.example.fitpassserver.admin.owner.dto.OwnerAdminResponseDTO;
import com.example.fitpassserver.owner.owner.entity.Owner;
import org.springframework.data.domain.Page;

import java.util.List;

public class OwnerAdminConverter {
    public static OwnerAdminResponseDTO.OwnerPagesDTO toPageDTO(Page<Owner> ownerPage) {
        List<OwnerAdminResponseDTO.OwnerInfosDTO> ownersInfo = ownerPage
                .map(owner -> OwnerAdminResponseDTO.OwnerInfosDTO.builder()
                        .id(owner.getId())
                        .name(owner.getName())
                        .corporation(owner.getCorporation())
                        .status(owner.getOwnerStatus().getKey())
                        .loginId(owner.getLoginId())
                        .phoneNumber(owner.getPhoneNumber())
                        .createdAt(owner.getCreatedAt().toLocalDate())
                        .build())
                .getContent();

        return OwnerAdminResponseDTO.OwnerPagesDTO.builder()
                .ownersInfo(ownersInfo)
                .totalPages(ownerPage.getTotalPages())
                .totalElements(ownerPage.getTotalElements())
                .build();
    }

    public static OwnerAdminResponseDTO.OwnerApprovalPagesDTO toApprovalPageDTO(Page<Owner> ownerPage) {
        List<OwnerAdminResponseDTO.OwnerApprovalsDTO> ownersApproval = ownerPage
                .map(owner -> OwnerAdminResponseDTO.OwnerApprovalsDTO.builder()
                        .id(owner.getId())
                        .name(owner.getName())
                        .corporation(owner.getCorporation())
                        .loginId(owner.getLoginId())
                        .phoneNumber(owner.getPhoneNumber())
                        .businessRegistrationUrl(owner.getBusinessRegistrationUrl())
                        .bankCopyUrl(owner.getBankCopyUrl())
                        .createdAt(owner.getCreatedAt().toLocalDate())
                        .build())
                .getContent();

        return OwnerAdminResponseDTO.OwnerApprovalPagesDTO.builder()
                .ownersApprovals(ownersApproval)
                .totalPages(ownerPage.getTotalPages())
                .totalElements(ownerPage.getTotalElements())
                .build();
    }

}
