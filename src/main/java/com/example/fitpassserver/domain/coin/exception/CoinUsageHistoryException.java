package com.example.fitpassserver.domain.coin.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class CoinUsageHistoryException extends GeneralException {

    public CoinUsageHistoryException(CoinUsageHistoryErrorCode code) {
        super(code);
    }
}
