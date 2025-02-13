package com.example.fitpassserver.domain.plan.entity;

import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PlanType {
    NONE("플랜 없음"),
    BASIC("배이직"),
    STANDARD("스탠다드"),
    PRO("프로");

    private final String name;

    public static PlanType getPlanType(String name) {
        return Arrays.stream(values())
                .filter(planType -> planType.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));
    }
}
