package com.example.fitpassserver.admin.member.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class MemberAdminResponseDTO {

    @Getter
    @Builder
    public static class MemberInfosDTO {
        private Long id;
        private String name;
        private String registerType;
        private String loginId;
        private String phoneNumber;
        private LocalDate createdAt;
        private String lastLoginAt;
    }

    @Getter
    @Builder
    public static class MemberPagesDTO {
        List<MemberInfosDTO> membersInfo;
        private int totalPages;
        private long totalElements;
    }
}
