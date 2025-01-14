package com.example.fitpassserver.domain.fitness.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class FitnessException extends GeneralException {
    public FitnessException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
