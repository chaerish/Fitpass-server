package com.example.fitpassserver.admin.member.converter;

import com.example.fitpassserver.admin.member.dto.response.MemberAdminResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.data.domain.Page;

public class MemberAdminConverter {

    public static MemberAdminResponseDTO.MemberPagesDTO toPageDTO(Page<Member> memberPage) {
        List<MemberAdminResponseDTO.MemberInfosDTO> membersInfo = memberPage
                .map(member -> MemberAdminResponseDTO.MemberInfosDTO.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .registerType(member.getProvider() != null ? "SNS 가입" : "회원 가입")
                        .loginId(member.getLoginId())
                        .phoneNumber(member.getPhoneNumber())
                        .createdAt(member.getCreatedAt().toLocalDate())
                        .lastLoginAt(formatLastLoginAt(member.getLastLoginAt()))
                        .build())
                .getContent();

        return MemberAdminResponseDTO.MemberPagesDTO.builder()
                .membersInfo(membersInfo)
                .totalPages(memberPage.getTotalPages())
                .totalElements(memberPage.getTotalElements())
                .build();
    }

    private static String formatLastLoginAt(LocalDateTime lastLoginAt) {
        if (lastLoginAt == null) {
            return "로그인 기록 없음";
        }

        LocalDate lastLoginDate = lastLoginAt.toLocalDate();
        long daysAgo = ChronoUnit.DAYS.between(lastLoginDate, LocalDate.now());

        if (daysAgo == 0) {
            return "오늘";
        } else {
            return daysAgo + "일전";
        }
    }
}
