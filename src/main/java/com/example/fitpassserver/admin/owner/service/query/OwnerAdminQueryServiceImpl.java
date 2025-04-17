package com.example.fitpassserver.admin.owner.service.query;

import com.example.fitpassserver.admin.owner.converter.OwnerAdminConverter;
import com.example.fitpassserver.admin.owner.dto.OwnerAdminResponseDTO;
import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import com.example.fitpassserver.owner.owner.entity.Owner;
import com.example.fitpassserver.owner.owner.entity.OwnerStatus;
import com.example.fitpassserver.owner.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class OwnerAdminQueryServiceImpl implements OwnerAdminQueryService {

    private final OwnerRepository ownerRepository;
    private final S3Service s3Service;

    private OwnerStatus convertToOwnerStatus(String keyword) {
        return Arrays.stream(OwnerStatus.values())
                .filter(status -> status.getKey().equals(keyword))
                .findFirst()
                .orElse(null);
    }


    @Override
    @Transactional(readOnly = true)
    public OwnerAdminResponseDTO.OwnerPagesDTO getOwnersInfo(int page, int size, String searchType, String keyword) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Owner> ownerPage;

        if (!StringUtils.hasText(searchType) || !StringUtils.hasText(keyword)) {
            ownerPage = ownerRepository.findAll(pageRequest);
        } else {
            switch (searchType) {
                case "phoneNumber":
                    ownerPage = ownerRepository.findByPhoneNumberContaining(keyword, pageRequest);
                    break;
                case "corporation":
                    ownerPage = ownerRepository.findByCorporationContaining(keyword, pageRequest);
                    break;
                case "status":
                    OwnerStatus status = convertToOwnerStatus(keyword);
                    if (status != null) {
                        ownerPage = ownerRepository.findByOwnerStatus(status, pageRequest);
                    } else {
                        ownerPage = Page.empty(pageRequest);
                    }
                    break;
                default:
                    ownerPage = ownerRepository.findAll(pageRequest);
            }
        }

        return OwnerAdminConverter.toPageDTO(ownerPage);
    }

    @Override
    @Transactional(readOnly = true)
    public OwnerAdminResponseDTO.OwnerApprovalPagesDTO getOwnersApproval(int page, int size, String searchType, String keyword) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Owner> ownerPage;

        if (!StringUtils.hasText(searchType) || !StringUtils.hasText(keyword)) {
            ownerPage = ownerRepository.findAll(pageRequest);
        } else {
            switch (searchType) {
                case "phoneNumber":
                    ownerPage = ownerRepository.findByPhoneNumberContaining(keyword, pageRequest);
                    break;
                case "corporation":
                    ownerPage = ownerRepository.findByCorporationContaining(keyword, pageRequest);
                    break;
                default:
                    ownerPage = ownerRepository.findAll(pageRequest);
            }
        }

        return OwnerAdminConverter.toApprovalPageDTO(ownerPage);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OwnerAdminResponseDTO.OwnerGetPresignedUrlDTO getFile(String loginId, String name) {
        Owner owner = ownerRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        String presignedUrl = null;

        if ("bankCopyUrl".equals(name)) {
            String bankCopyKey = owner.getBankCopyUrl();
            if (bankCopyKey != null) {
                presignedUrl = s3Service.getGetS3Url(owner.getId(), bankCopyKey).getPreSignedUrl();
            }
        } else if ("businessRegistrationUrl".equals(name)) {
            String businessRegKey = owner.getBusinessRegistrationUrl();
            if (businessRegKey != null) {
                presignedUrl = s3Service.getGetS3Url(owner.getId(), businessRegKey).getPreSignedUrl();
            }
        }
        return OwnerAdminResponseDTO.OwnerGetPresignedUrlDTO.builder()
                .loginId(loginId)
                .name(name)
                .url(presignedUrl)
                .build();
    }
}
