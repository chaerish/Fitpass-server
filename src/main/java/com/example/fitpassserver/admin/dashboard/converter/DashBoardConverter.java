package com.example.fitpassserver.admin.dashboard.converter;

import com.example.fitpassserver.admin.dashboard.dto.response.DashBoardResponseDTO;
import com.example.fitpassserver.admin.dashboard.entity.DashBoard;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public class DashBoardConverter {

    public static DashBoard toDashBoard(LocalDate date,
                                        int newMemberCount,
                                        int visitant,
                                        int pageView,
                                        int buyPass,
                                        int usePass) {
        return DashBoard.builder()
                .date(date)
                .newMemberCount(newMemberCount)
                .visitant(visitant)
                .pageView(pageView)
                .buyPass(buyPass)
                .usePass(usePass)
                .build();
    }

    public static DashBoardResponseDTO.DashBoardInfoDTO toDashBoardInfoDTO(DashBoard dashBoard) {
        return DashBoardResponseDTO.DashBoardInfoDTO.builder()
                .date(dashBoard.getDate())
                .newMemberCount(dashBoard.getNewMemberCount())
                .visitant(dashBoard.getVisitant())
                .pageView(dashBoard.getPageView())
                .buyPass(dashBoard.getBuyPass())
                .usePass(dashBoard.getUsePass())
                .build();
    }

    public static DashBoardResponseDTO.DashBoardInfoListDTO toDashBoardInfoListDTO(Page<DashBoard> dashBoardList) {
        return DashBoardResponseDTO.DashBoardInfoListDTO.builder()
                .items(dashBoardList.getContent().stream().map(DashBoardConverter::toDashBoardInfoDTO).toList())
                .pageNo(dashBoardList.getNumber())
                .totalPage(dashBoardList.getTotalPages())
                .size(dashBoardList.getSize())
                .build();
    }
}
