package com.example.fitpassserver.global.jwt.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(JwtErrorCode code) {
        super(code);
    }
}