package com.example.fitpassserver.global.aws.s3.util;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.fitpassserver.global.aws.s3.exception.S3ErrorCode;
import com.example.fitpassserver.global.aws.s3.exception.S3Exception;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final String bucket;

    public S3Uploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    public String upload(MultipartFile file, String filePath) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucket, filePath, file.getInputStream(), metadata);

            return amazonS3.getUrl(bucket, filePath).toString();
        } catch (IOException e) {
            throw new S3Exception(S3ErrorCode.UPLOAD_FAILED);
        }
    }
}