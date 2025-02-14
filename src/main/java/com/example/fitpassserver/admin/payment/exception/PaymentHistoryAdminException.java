package com.example.fitpassserver.admin.payment.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class PaymentHistoryAdminException extends GeneralException {
    public PaymentHistoryAdminException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}