package com.example.fitpassserver.owner.fitness.service;

import com.example.fitpassserver.admin.fitness.converter.FitnessAdminConverter;
import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.admin.fitness.dto.response.FitnessAdminResponseDTO;
import com.example.fitpassserver.domain.fitness.converter.CategoryConverter;
import com.example.fitpassserver.domain.fitness.converter.FitnessImageConverter;
import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.FitnessImage;
import com.example.fitpassserver.domain.fitness.exception.FitnessErrorCode;
import com.example.fitpassserver.domain.fitness.exception.FitnessException;
import com.example.fitpassserver.domain.fitness.repository.FitnessImageRepository;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import com.example.fitpassserver.owner.fitness.converter.FitnessOwnerConverter;
import com.example.fitpassserver.owner.fitness.dto.request.FitnessOwnerRequestDTO;
import com.example.fitpassserver.owner.fitness.dto.response.FitnessOwnerResponseDTO;
import com.example.fitpassserver.owner.owner.entity.Owner;
import com.example.fitpassserver.owner.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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
@Slf4j
public class FitnessOwnerServiceImpl implements FitnessOwnerService {

    private final FitnessRepository fitnessRepository;
    private final FitnessImageRepository fitnessImageRepository;
    private final S3Service s3Service;
    private final OwnerRepository ownerRepository;

    private String generateMainImageKey(Long fitnessId, String originalFilename){
        return String.format("fitness/%d/main/%s/%s", fitnessId, UUID.randomUUID(), originalFilename);
    }
    private String generateAdditionalImageKey(Long fitnessId, String originalFilename){
        return String.format("fitness/%d/additional/%s/%s", fitnessId, UUID.randomUUID(), originalFilename);
    }

    @Override
    public Long createFitness(MultipartFile mainImage, List<MultipartFile> additionalImages, FitnessOwnerRequestDTO.FitnessRequestDTO dto, String loginId) throws IOException {
        // 우선 Fitness 엔티티를 생성
        Fitness fitness = FitnessOwnerConverter.toEntity(dto);
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
        Owner owner = ownerRepository.findByLoginId(loginId).orElseThrow(
                () ->new MemberException(MemberErrorCode.NOT_FOUND));


        // fitness 맴버 매핑
        fitness.setOwner(owner);

        // 시설 저장
        fitnessRepository.save(fitness);

        return fitness.getId();
    }

    @Override
    public FitnessOwnerResponseDTO.FitnessListDTO getFitnessList(Long ownerId, Long cursor, int size) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(
                () -> new MemberException(MemberErrorCode.NOT_FOUND));

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        Slice<Fitness> fitnesses;

        // 커서 체크 로직 수정
        if (cursor == null || cursor == 0L) {
            fitnesses = fitnessRepository.findFirstPageByOwnerId(ownerId, pageable);
        } else {
            fitnesses = fitnessRepository.findByOwnerAndCursor(ownerId, cursor, pageable);
        }

        return FitnessOwnerConverter.toFitnessPageResDTO(fitnesses);
    }

    @Override
    public FitnessAdminResponseDTO.FitnessInfoDTO updateFitness(Long ownerId, Long fitnessId, FitnessOwnerRequestDTO.FitnessRequestDTO dto) {
        Fitness fitness = fitnessRepository.findById(fitnessId).orElseThrow(
                () -> new FitnessException(FitnessErrorCode.FITNESS_NOT_FOUND));

        if(!fitness.getOwner().getId().equals(ownerId)){
            throw new MemberException(MemberErrorCode.INVALID_INFO);
        }
        List<Category> categoryList = CategoryConverter.toEntityList(dto.getCategoryList(), fitness);
        fitness.setCategoryList(categoryList);

        fitness.update(
                dto.getFitnessName(),
                dto.getAddress(),
                dto.getDetailAddress(),
                dto.getPhoneNumber(),
                dto.getFee(),
                dto.getTotalFee(),
                categoryList,
                dto.isPurchasable(),
                dto.getNotice(),
                dto.getTime(),
                dto.getHowToUse(),
                dto.getLatitude(),
                dto.getLongitude()
        );

        return FitnessAdminConverter.from(fitness);
    }
}