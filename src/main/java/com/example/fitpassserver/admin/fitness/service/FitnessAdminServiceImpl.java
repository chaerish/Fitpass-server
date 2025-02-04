package com.example.fitpassserver.admin.fitness.service;

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
import com.example.fitpassserver.domain.member.entity.Role;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FitnessAdminServiceImpl implements FitnessAdminService{
    private final MemberRepository memberRepository;
    private final FitnessRepository fitnessRepository;
    private final FitnessImageRepository fitnessImageRepository;
    private final S3Service s3Service;

    @Override
    public Long createFitness(Member member, MultipartFile mainImage, List<MultipartFile> additionalImages, FitnessAdminRequestDTO.CreateFitnessDTO dto) throws IOException {

        // 메인 이미지 업로드 - key만 저장
        String mainImageKey = generateImageKey(mainImage.getOriginalFilename());
        s3Service.uploadFile(mainImage, mainImageKey);

        // 추가 이미지 업로드 - key만 저장
        List<String> additionalImageUrls = new ArrayList<>();
        if(additionalImages != null && !additionalImages.isEmpty()){
            for(MultipartFile image : additionalImages){
                String imageKey = generateImageKey(image.getOriginalFilename());
                s3Service.uploadFile(image, imageKey);
                additionalImageUrls.add(imageKey);
            }
        }
        // 시설 엔티티 생성
        Fitness fitness = FitnessAdminConverter.toEntity(dto, mainImageKey, additionalImageUrls);

        // 카테고리 생성
        List<Category> categoryList = CategoryConverter.toEntityList(dto.getCategoryList(), fitness);
        fitness.setCategoryList(categoryList);

        // 어드민 매핑
        if(member.getRole().equals(Role.ADMIN)){
            fitness.setAdmin(member);
        }
        // 시설 저장
        fitnessRepository.save(fitness);

        if(!additionalImageUrls.isEmpty()){
            List<FitnessImage> fitnessImages = FitnessImageConverter.saveFitnessImage(additionalImageUrls, fitness);
            fitnessImageRepository.saveAll(fitnessImages);
        }

        return fitness.getId();
    }

    private String generateImageKey(String originalFilename){
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }
}
