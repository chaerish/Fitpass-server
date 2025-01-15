package com.example.fitpassserver.domain.coinPaymentHistory.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class KakaoPayException extends GeneralException {
    public KakaoPayException(KakaoPayErrorCode code) {
        super(code);
    }
}
