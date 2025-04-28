package com.example.fitpassserver.domain.profile.controller;

import com.example.fitpassserver.domain.member.annotation.CurrentMember;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.profile.dto.ProfileResponseDTO;
import com.example.fitpassserver.domain.profile.service.ProfileService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.global.aws.s3.dto.S3UrlResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/member/profile")
@Tag(name = "프로필 API", description = "프로필 조회 및 변경 API입니다.")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("")
    @Operation(summary = "프로필 조회 API", description = "사용자의 ID로 프로필을 조회하는 api")
    public ApiResponse<ProfileResponseDTO.GetProfileDTO> getProfile(@CurrentMember Member member) {
        ProfileResponseDTO.GetProfileDTO profile = profileService.getProfile(member);
        return ApiResponse.onSuccess(profile);
    }


    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "프로필 변경 API", description = "프로필 이미지를 업로드하고 저장")
    public ApiResponse<Long> updateProfile(@CurrentMember Member member,
                                           @RequestParam("file") MultipartFile file) throws IOException {
        Long profileId = profileService.updateProfile(member, file);
        return ApiResponse.onSuccess(profileId);
    }

    @DeleteMapping("")
    @Operation(summary = "프로필 삭제 API", description = "사용자의 프로필 이미지를 삭제하는 API")
    public ApiResponse<Void> deleteProfile(@CurrentMember Member member) {
        profileService.deleteProfile(member);
        return ApiResponse.onSuccess(null);
    }

    /**
     * presigned url ver
     **/
    @GetMapping("/upload-url")
    @Operation(summary = "프로필 업로드 Presigned URL 요청 API", description = "S3에 업로드할 Presigned URL을 가져오는 API")
    public ApiResponse<S3UrlResponseDTO> getUploadUrl(
            @CurrentMember Member member) {
        return ApiResponse.onSuccess(profileService.getProfileUploadUrl(member));
    }


    @PatchMapping("/update")
    @Operation(summary = "프로필 변경 API", description = "S3에 업로드 후 받은 key를 백엔드로 전달하는 API(업로드 -> 변경 순으로 사용하면 됩니다.)")
    public ApiResponse<Long> updateProfile(
            @CurrentMember Member member,
            @RequestParam("key") String key) {
        Long profileId = profileService.updatePresignedUrlProfile(member, key);
        return ApiResponse.onSuccess(profileId);
    }
}