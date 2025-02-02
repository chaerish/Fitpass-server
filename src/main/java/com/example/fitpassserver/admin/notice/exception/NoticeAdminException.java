package com.example.fitpassserver.admin.notice.exception;

import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class NoticeAdminException extends GeneralException {
    public NoticeAdminException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}