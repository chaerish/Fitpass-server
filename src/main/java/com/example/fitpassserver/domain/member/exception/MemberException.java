package com.example.fitpassserver.domain.member.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class MemberException extends GeneralException {
    public MemberException(MemberErrorCode code) {
        super(code);
    }
}
