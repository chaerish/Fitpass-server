package com.example.fitpassserver.global.aws.s3.controller;

import com.example.fitpassserver.global.aws.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

//    @GetMapping(value = "/auth/profile-image/post")
//    public ApiResponse<S3UrlResponseDTO> getPostS3Url(@CurrentMember Member member, String filename) {
//        S3UrlResponseDTO getS3UrlResponseDto = s3Service.getPostS3Url(member.getId(), filename);
//        return ApiResponse.onSuccess(getS3UrlResponseDto);
//    }
//
//    @GetMapping(value = "/auth/profile-image/get")
//    public ApiResponse<S3UrlResponseDTO> getGetS3Url(@CurrentMember Member member, @RequestParam String key) {
//        S3UrlResponseDTO getS3UrlResponseDto = s3Service.getGetS3Url(member.getId(), key);
//        return ApiResponse.onSuccess(getS3UrlResponseDto);
//    }
}
