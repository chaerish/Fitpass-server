package com.example.fitpassserver.global.aws.s3.exception;

import com.example.fitpassserver.global.apiPayload.exception.GeneralException;

public class S3Exception extends GeneralException {
    public S3Exception(S3ErrorCode code) {
        super(code);
    }
}
