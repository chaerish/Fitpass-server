package com.example.fitpassserver.admin.fitness.service;

import com.example.fitpassserver.admin.fitness.converter.FitnessAdminConverter;
import com.example.fitpassserver.admin.fitness.dto.request.FitnessAdminRequestDTO;
import com.example.fitpassserver.admin.fitness.dto.response.FitnessAdminResponseDTO;
import com.example.fitpassserver.domain.fitness.converter.CategoryConverter;
import com.example.fitpassserver.domain.fitness.converter.FitnessImageConverter;
import com.example.fitpassserver.domain.fitness.entity.Category;
import com.example.fitpassserver.domain.fitness.entity.Fitness;
import com.example.fitpassserver.domain.fitness.entity.FitnessImage;
import com.example.fitpassserver.domain.fitness.repository.FitnessImageRepository;
import com.example.fitpassserver.domain.fitness.repository.FitnessRepository;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final FitnessRepository fitnessRepository;
    private final FitnessImageRepository fitnessImageRepository;
    private final S3Service s3Service;

    @Override
    public Long createFitness(MultipartFile mainImage, List<MultipartFile> additionalImages, FitnessAdminRequestDTO.CreateFitnessDTO dto) throws IOException {

        // 우선 Fitness 엔티티를 생성 (fitnessId 필요)
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

        // 시설 저장
        fitnessRepository.save(fitness);

        return fitness.getId();
    }

    @Override
    public FitnessAdminResponseDTO.FitnessListDTO getFitnessList(int page, int size, String sort, String sortDirection, String searchType, String keyword) {
        Sort sorting = createSort(sort, sortDirection);
        PageRequest pageRequest = PageRequest.of(page, size, sorting);

        Page<Fitness> fitnessPage;
        if (StringUtils.isEmpty(searchType) || StringUtils.isEmpty(keyword)) {
            // 검색 조건이 없으면 전체 조회
            fitnessPage = fitnessRepository.findAll(pageRequest);
        } else {
            switch (searchType) {
                case "name":
                    fitnessPage = fitnessRepository.findByNameContaining(keyword, pageRequest);
                    break;
                case "category":
                    fitnessPage = fitnessRepository.findByCategoryList_NameContaining(keyword, pageRequest);
                    break;
                case "phoneNumber":
                    fitnessPage = fitnessRepository.findByPhoneNumberContaining(keyword, pageRequest);
                    break;
                default:
                    fitnessPage = fitnessRepository.findAll(pageRequest);
            }
        }

        return FitnessAdminConverter.from(fitnessPage);
    }

    private Sort createSort(String sort, String sortDirection) {
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

        if (StringUtils.isEmpty(sort)) {
            return Sort.by(direction, "createdAt");
        }

        return switch (sort) {
            case "name" -> Sort.by(direction, "name");
            case "category" -> Sort.by(direction, "categoryList");
            case "totalFee" -> Sort.by(direction, "totalFee");
            case "phoneNumber" -> Sort.by(direction, "phoneNumber");
            case "status" -> Sort.by(direction, "isPurchasable");
            default -> Sort.by(direction, "createdAt");
        };
    }

    private String generateMainImageKey(Long fitnessId, String originalFilename){
        return String.format("fitness/%d/main/%s/%s", fitnessId, UUID.randomUUID(), originalFilename);
    }
    private String generateAdditionalImageKey(Long fitnessId, String originalFilename){
        return String.format("fitness/%d/additional/%s/%s", fitnessId, UUID.randomUUID(), originalFilename);
    }

}
