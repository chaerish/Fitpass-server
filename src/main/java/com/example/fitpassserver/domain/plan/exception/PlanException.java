package com.example.fitpassserver.domain.plan.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class PlanException extends GeneralException {
    public PlanException(PlanErrorCode code) {
        super(code);
    }
}
