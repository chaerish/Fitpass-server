package com.example.fitpassserver.domain.profile.service;


import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import com.example.fitpassserver.domain.profile.dto.ProfileResponseDTO;
import com.example.fitpassserver.domain.profile.entity.Profile;
import com.example.fitpassserver.domain.profile.exception.ProfileErrorCode;
import com.example.fitpassserver.domain.profile.exception.ProfileException;
import com.example.fitpassserver.domain.profile.repositroy.ProfileRepository;
import com.example.fitpassserver.global.aws.s3.dto.S3UrlResponseDTO;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final CoinRepository coinRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /* 프로필 변경 */
    @Transactional
    public Long updateProfile(Member member, MultipartFile file) throws IOException {
        Long memberId = member.getId();
        //멤버 확인
        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        //프로필 유무 확인
        Profile profile = profileRepository.findProfileByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.NOT_FOUND));

        log.info("Found Profile: {}", profile);

        //기존 프로필 이미지 버킷에서 삭제
        if (!"none".equals(profile.getPictureKey())) {
            s3Service.deleteFile(profile.getId());
        }

        //key 생성
        S3UrlResponseDTO s3UrlResponseDTO = s3Service.getPostS3Url(memberId, file.getOriginalFilename());
        String key = s3UrlResponseDTO.getKey();

        //image 업로드
        String uploadedUrl = s3Service.uploadFile(file, key);

        //key 변경
        profile.updateProfile(key);
        entityManager.flush();

        return profile.getId();
    }

    /* 프로필 조회 */
    @Transactional(readOnly = true)
    public ProfileResponseDTO.GetProfileDTO getProfile(Member member) {
        Long memberId = member.getId();

        Profile profile = profileRepository.findProfileByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.NOT_FOUND));

        System.out.println(profile.getId());
        log.info("Found Profile: {}", profile);

        // Plan 조회
        Plan plan = planRepository.findByMemberId(profile.getMember().getId())
                .orElse(null);

        //presigned url 생성
        String presignedUrl;
        if (profile.getPictureKey() != null && !profile.getPictureKey().equals("none")) {
            presignedUrl = s3Service.getGetS3Url(memberId, profile.getPictureKey()).getPreSignedUrl();
        } else {
            presignedUrl = "none";
        }
        List<Coin> coinList = coinRepository.findAllByMemberAndExpiredDateBefore(member, LocalDate.now());
        Long coinQuantity = coinRepository.findAllByMemberAndExpiredDateBefore(member, LocalDate.now()).stream()
                .mapToLong(Coin::getCount).sum();
        return ProfileResponseDTO.GetProfileDTO.builder()
                .id(profile.getId())
                .pictureUrl(presignedUrl)
                .coinQuantity(coinQuantity)
                .pictureKey(profile.getPictureKey())
                .name(profile.getMember().getName())
                .planType(plan != null ? plan.getPlanType().name() : null) // 패스 없으면 null 값
                .build();
    }

    /* 프로필 삭제 -> 기본 이미지 변경시 */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteProfile(Member member) {
        Long memberId = member.getId();

        Profile profile = profileRepository.findProfileByMemberId(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // S3에서 파일 삭제
        s3Service.deleteFile(profile.getId());

        //key 변경 -> none
        profile.updateProfile("none");
        entityManager.flush();

    }


}
