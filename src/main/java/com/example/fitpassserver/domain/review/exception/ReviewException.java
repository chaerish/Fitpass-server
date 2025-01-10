package com.example.fitpassserver.domain.review.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class ReviewException extends GeneralException {
    public ReviewException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
