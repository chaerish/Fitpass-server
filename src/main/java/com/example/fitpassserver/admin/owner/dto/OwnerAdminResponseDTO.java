package com.example.fitpassserver.admin.owner.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class OwnerAdminResponseDTO {
    /**
     * 시설 회원 정보 list
     **/
    @Getter
    @Builder
    public static class OwnerInfosDTO {
        private Long id;
        private String name;
        private String corporation;
        private String loginId;
        private String phoneNumber;
        private LocalDate createdAt;
        private String status;
    }

    @Getter
    @Builder
    public static class OwnerPagesDTO {
        List<OwnerAdminResponseDTO.OwnerInfosDTO> ownersInfo;
        private int totalPages;
        private long totalElements;
    }

    /**
     * 시설 회원 승인 요청 list
     **/
    @Getter
    @Builder
    public static class OwnerApprovalsDTO {
        private Long id;
        private String name;
        private String corporation;
        private String loginId;
        private String phoneNumber;
        private LocalDate createdAt;
        private String businessRegistrationUrl;
        private String bankCopyUrl;
    }

    @Getter
    @Builder
    public static class OwnerApprovalPagesDTO {
        List<OwnerAdminResponseDTO.OwnerApprovalsDTO> ownersApprovals;
        private int totalPages;
        private long totalElements;
    }
}
