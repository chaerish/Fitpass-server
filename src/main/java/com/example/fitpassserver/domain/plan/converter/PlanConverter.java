package com.example.fitpassserver.domain.plan.converter;

import com.example.fitpassserver.domain.plan.dto.response.PlanStatusResponseDTO;

public class PlanConverter {
    //flag false일때 사용하는 converter
    public static PlanStatusResponseDTO toPlanStatusResultDTO(boolean flag) {
        return PlanStatusResponseDTO.builder()
                .itemName("NONE")
                .available(flag)
                .build();
    }
}
