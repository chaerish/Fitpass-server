package com.example.fitpassserver.global.apiPayload.exception.base;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class FailureException extends GeneralException {

    public FailureException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
