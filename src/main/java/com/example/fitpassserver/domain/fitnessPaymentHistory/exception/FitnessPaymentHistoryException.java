package com.example.fitpassserver.domain.fitnessPaymentHistory.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class FitnessPaymentHistoryException extends GeneralException {

    public FitnessPaymentHistoryException(FitnessPaymentHistoryErrorCode code) {
        super(code);
    }
}
