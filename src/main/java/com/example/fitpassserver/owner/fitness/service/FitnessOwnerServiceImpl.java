package com.example.fitpassserver.owner.fitness.service;

import com.example.fitpassserver.admin.fitness.converter.FitnessAdminConverter;
import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.domain.fitness.converter.CategoryConverter;
import com.example.fitpassserver.domain.fitness.converter.FitnessImageConverter;
import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.FitnessImage;
import com.example.fitpassserver.domain.fitness.repository.FitnessImageRepository;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.entity.MemberStatus;
import com.example.fitpassserver.domain.member.entity.Role;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FitnessOwnerServiceImpl implements FitnessOwnerService {

    private final FitnessRepository fitnessRepository;
    private final FitnessImageRepository fitnessImageRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;

    private String generateMainImageKey(Long fitnessId, String originalFilename){
        return String.format("fitness/%d/main/%s/%s", fitnessId, UUID.randomUUID(), originalFilename);
    }
    private String generateAdditionalImageKey(Long fitnessId, String originalFilename){
        return String.format("fitness/%d/additional/%s/%s", fitnessId, UUID.randomUUID(), originalFilename);
    }

    @Override
    public Long createFitness(MultipartFile mainImage, List<MultipartFile> additionalImages, FitnessAdminRequestDTO.FitnessReqDTO dto, Long memberId) throws IOException {
        // 우선 Fitness 엔티티를 생성
        Fitness fitness = FitnessAdminConverter.toEntity(dto);
        fitnessRepository.save(fitness); // ID 생성됨
        Long fitnessId = fitness.getId(); // 생성된 ID 가져오기

        // 메인 이미지 업로드 - key만 저장
        String mainImageKey = generateMainImageKey(fitnessId, mainImage.getOriginalFilename());
        s3Service.uploadFile(mainImage, mainImageKey);
        fitness.setMainImage(mainImageKey);

        // 추가 이미지 업로드 - key만 저장
        List<String> additionalImageUrls = new ArrayList<>();
        if(additionalImages != null && !additionalImages.isEmpty()){
            for(MultipartFile image : additionalImages){
                String imageKey = generateAdditionalImageKey(fitnessId, image.getOriginalFilename());
                s3Service.uploadFile(image, imageKey);
                additionalImageUrls.add(imageKey);
            }
        }

        // 카테고리 생성
        List<Category> categoryList = CategoryConverter.toEntityList(dto.getCategoryList(), fitness);
        fitness.setCategoryList(categoryList);


        if(!additionalImageUrls.isEmpty()){
            List<FitnessImage> fitnessImages = FitnessImageConverter.saveFitnessImage(additionalImageUrls, fitness);
            fitnessImageRepository.saveAll(fitnessImages);
            fitness.setAdditionalImages(fitnessImages);
        }

        // 사업자 조회
        Member member = memberRepository.findByIdAndStatusIs(memberId, MemberStatus.ACTIVE)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INACTIVE_ACCOUNT));

        // 해당 전화번호의 사용자가 OWNER, ADMIN인지확인
        if(!(member.getRole().equals(Role.ADMIN) || member.getRole().equals(Role.OWNER))){
            throw new MemberException(MemberErrorCode.INVALID_ROLE);
        }

        // fitness 맴버 매핑
        fitness.setMember(member);

        // 시설 저장
        fitnessRepository.save(fitness);

        return fitness.getId();
    }
}
