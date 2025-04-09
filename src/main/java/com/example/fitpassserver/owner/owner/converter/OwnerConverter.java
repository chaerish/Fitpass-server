package com.example.fitpassserver.owner.owner.converter;

import com.example.fitpassserver.domain.member.entity.Role;
import com.example.fitpassserver.owner.owner.dto.OwnerRequestDTO;
import com.example.fitpassserver.owner.owner.dto.OwnerResponseDTO;
import com.example.fitpassserver.owner.owner.entity.Owner;

import java.time.LocalDateTime;

public class OwnerConverter {

    public static OwnerResponseDTO.JoinResultDTO toJoinResultDTO(Owner owner) {
        return OwnerResponseDTO.JoinResultDTO.builder()
                .ownerId(owner.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Owner toOwner(OwnerRequestDTO.OwnerJoinDTO request) {
        Role role = Role.OWNER;
        return Owner.builder()
                .loginId(request.getLoginId())
                .name(request.getName())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .corporation(request.getCorporation())
                .businessRegistrationNumber(request.getBusinessRegistrationNumber())
                .bankName(request.getBankName())
                .depositAccountName(request.getDepositAccountName())
                .depositAccountNumber(request.getDepositAccountNumber())
                .businessRegistrationUrl(request.getBusinessRegistrationUrl())
                .bankCopyUrl(request.getBankCopyUrl())
                .isAgree(request.isAgree())
                .isTermsAgreed(request.isTermsAgreed())
                .isPersonalInformaionAgreed(request.isPersonalInformationAgreed())
                .isThirdPartyAgreed(request.isThirdPartyAgreed())
                .isMarketingAgreed(request.isMarketingAgreed())
                .isAdditionalInfo(false)
                .build();
    }
}
