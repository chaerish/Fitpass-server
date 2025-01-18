package com.example.fitpassserver.domain.notice.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class NoticeException extends GeneralException {
    public NoticeException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}