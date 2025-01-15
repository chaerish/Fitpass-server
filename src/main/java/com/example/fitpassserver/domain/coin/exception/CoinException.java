package com.example.fitpassserver.domain.coin.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class CoinException extends GeneralException {
    public CoinException(CoinErrorCode code) {
        super(code);
    }
}
