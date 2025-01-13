package com.example.fitpassserver.domain.fitness.entity.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class MemberFitnessException extends GeneralException {
    public MemberFitnessException(MemberFitnessErrorCode code) {
        super(code);
    }
}
