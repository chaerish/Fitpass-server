package com.example.fitpassserver.domain.profile.service;


import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import com.example.fitpassserver.domain.profile.dto.ProfileRequestDTO;
import com.example.fitpassserver.domain.profile.dto.ProfileResponseDTO;
import com.example.fitpassserver.domain.profile.entity.Profile;
import com.example.fitpassserver.domain.profile.exception.ProfileErrorCode;
import com.example.fitpassserver.domain.profile.exception.ProfileException;
import com.example.fitpassserver.domain.profile.repositroy.ProfileRepository;
import com.example.fitpassserver.global.aws.s3.dto.S3UrlResponseDTO;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import com.example.fitpassserver.global.aws.s3.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PlanRepository planRepository;
    private final S3Service s3Service;
    private final S3Uploader s3Uploader;

    /* 프로필 변경 */
    public Long updateProfile(Long memberId, ProfileRequestDTO.PutProfileDTO putProfileDTO) {
        Profile profile = profileRepository.findProfileByMemberId(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        //s3 url 생성
        S3UrlResponseDTO s3UrlResponseDTO = s3Service.getPostS3Url(memberId, putProfileDTO.getKey());

        //기존 프로필 이미지 삭제
        if (profile.getPictureKey() != null) {
            s3Service.deleteFile(profile.getPictureKey());
        }

        profile.updateProfile(s3UrlResponseDTO.getKey());
        return profile.getId();
    }

    /* 프로필 조회 */
    @Transactional(readOnly = true)
    public ProfileResponseDTO.GetProfileDTO getProfile(Long memberId) {
        Profile profile = profileRepository.findProfileByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.NOT_FOUND));

        // Plan 조회 -> 없으면 null로 저장
        Plan plan = planRepository.findByMemberId(profile.getMember().getId())
                .orElse(null);

        //converter
        ProfileResponseDTO.GetProfileDTO getProfileDTO = ProfileResponseDTO.GetProfileDTO.builder()
                .id(profile.getId())
                .pictureUrl(createPictureUrl(memberId, profile))
                .pictureKey(profile.getPictureKey())
                .name(profile.getMember().getName())
                .planName(plan != null ? plan.getPlanName().name() : null) // 패스 없으면 null 값
                .build();

        return getProfileDTO;
    }

    /* 프로필 생성 */
    private String createPictureUrl(Long memberId, Profile profile) {
        if (profile.getPictureKey() == null) {
            return profile.getPictureUrl();
        }
        return s3Service.getGetS3Url(memberId,
                profile.getPictureKey()).getPreSignedUrl();
    }

    /* 프로필 삭제 -> 기본 이미지 변경시 */
    @Transactional
    public void deleteProfile(Long memberId) {
        Profile profile = profileRepository.findProfileByMemberId(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // S3에서 파일 삭제
        if (profile.getPictureKey() != null) {
            s3Service.deleteFile(profile.getPictureKey());
        }

        // 기본 프로필 이미지로 초기화
        profile.updateProfile(null);
    }

    public String updateProfileImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        String filePath = "profile-images/" + UUID.randomUUID() + "-" + image.getOriginalFilename();
        return s3Uploader.upload(image, filePath);
    }


}
