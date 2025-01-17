package com.example.fitpassserver.domain.profile.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class ProfileException extends GeneralException {
    public ProfileException(ProfileErrorCode code) {
        super(code);
    }
}
