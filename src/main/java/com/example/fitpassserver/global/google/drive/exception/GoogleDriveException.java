package com.example.fitpassserver.global.google.drive.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class GoogleDriveException extends GeneralException {
    public GoogleDriveException(GoogleDriveErrorCode code) {
        super(code);
    }
}
