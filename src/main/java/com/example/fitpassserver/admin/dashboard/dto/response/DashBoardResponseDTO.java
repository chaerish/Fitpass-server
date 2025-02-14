package com.example.fitpassserver.admin.dashboard.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class DashBoardResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DashBoardInfoDTO {
        private int newMemberCount;
        private int visitant;
        private int pageView;
        private int buyPass;
        private int usePass;
        private LocalDate date;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DashBoardInfoListDTO {
        private List<DashBoardInfoDTO> items;
        private int pageNo;
        private int totalPage;
        private int size;
    }
}
