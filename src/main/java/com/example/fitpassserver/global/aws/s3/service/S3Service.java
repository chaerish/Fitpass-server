package com.example.fitpassserver.global.aws.s3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.fitpassserver.domain.profile.entity.Profile;
import com.example.fitpassserver.domain.profile.exception.ProfileErrorCode;
import com.example.fitpassserver.domain.profile.exception.ProfileException;
import com.example.fitpassserver.domain.profile.repositroy.ProfileRepository;
import com.example.fitpassserver.global.aws.s3.dto.S3UrlResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3 amazonS3Client;
    private final ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager entityManager;


    // 버킷 이름
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional(readOnly = true)
    public S3UrlResponseDTO getPostS3Url(Long memberId, String filename) {
        // filename 설정하기(profile 경로 + 멤버ID + 랜덤 값)
        String key = "profile/" + memberId + "/" + UUID.randomUUID() + "/" + filename;

        // url 유효기간 설정(1시간)
        Date expiration = getExpiration();

        // presigned url 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                getPostGeneratePresignedUrlRequest(key, expiration);

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return S3UrlResponseDTO.builder()
                .preSignedUrl(url.toExternalForm())
                .key(key)
                .build();
    }

    //presigned url로 파일 업로드
    @Transactional
    public String uploadFile(MultipartFile file, String key) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        //s3에 파일 업로드
        amazonS3Client.putObject(new PutObjectRequest(bucket, key, file.getInputStream(), metadata));

        return amazonS3Client.getUrl(bucket, key).toString();
    }

    @Transactional(readOnly = true)
    public S3UrlResponseDTO getGetS3Url(Long memberId, String key) {
        // url 유효기간 설정(1시간)
        Date expiration = getExpiration();

        // presigned url 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                getGetGeneratePresignedUrlRequest(key, expiration);

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return S3UrlResponseDTO.builder()
                .preSignedUrl(url.toExternalForm())
                .key(key)
                .build();
    }

    /* post 용 URL 생성하는 메소드 */
    private GeneratePresignedUrlRequest getPostGeneratePresignedUrlRequest(String fileName, Date expiration) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest
                = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withKey(fileName)
                .withExpiration(expiration);
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    /* get 용 URL 생성하는 메소드 */
    private GeneratePresignedUrlRequest getGetGeneratePresignedUrlRequest(String key, Date expiration) {
        return new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
    }

    // url 유효기간 생성
    private static Date getExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60; // 1시간으로 설정하기
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    @Transactional
    public void deleteFile(Long profileId) {
        //버킷에서 삭제
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.NOT_FOUND));

        String key = profile.getPictureKey();

        if (key != null && !key.equals("none")) {
            if (amazonS3Client.doesObjectExist(bucket, key)) {
                amazonS3Client.deleteObject(bucket, key);
                log.info("Deleted file with key: " + key);
            } else {
                log.error("File not found in S3: " + key);
                throw new ProfileException(ProfileErrorCode.FILE_NOT_FOUND);
            }

        } else {
            log.info("No picture to delete for profileId: " + profileId);
        }

    }

    @Transactional(readOnly = true)
    public S3UrlResponseDTO getPostS3UrlExceptFileName(Long memberId) {
        // filename 설정하기(profile 경로 + 멤버ID + 랜덤 값)
        String key = "profile/" + memberId + "/" + UUID.randomUUID();

        // url 유효기간 설정(1시간)
        Date expiration = getExpiration();

        // presigned url 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                getPostGeneratePresignedUrlRequest(key, expiration);

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return S3UrlResponseDTO.builder()
                .preSignedUrl(url.toExternalForm())
                .key(key)
                .build();
    }

}
