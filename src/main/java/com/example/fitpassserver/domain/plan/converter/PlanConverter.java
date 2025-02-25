package com.example.fitpassserver.domain.plan.converter;

import com.example.fitpassserver.domain.plan.dto.response.PlanStatusResponseDTO;

public class PlanConverter {
    //flag false일때 사용하는 converter
    static final String NONE = "NONE";

    public static PlanStatusResponseDTO toNoPlanStatusResultDTO() {
        return PlanStatusResponseDTO.builder()
                .itemName(NONE)
                .available(false)
                .build();
    }
}
