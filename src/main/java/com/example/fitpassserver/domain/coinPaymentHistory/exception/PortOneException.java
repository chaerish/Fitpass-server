package com.example.fitpassserver.domain.coinPaymentHistory.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class PortOneException extends GeneralException {

    public PortOneException(PortOneErrorCode code) {
        super(code);
    }
}
