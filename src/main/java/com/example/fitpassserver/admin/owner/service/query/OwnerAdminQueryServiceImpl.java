package com.example.fitpassserver.admin.owner.service.query;

import com.example.fitpassserver.admin.owner.converter.OwnerAdminConverter;
import com.example.fitpassserver.admin.owner.dto.OwnerAdminResponseDTO;
import com.example.fitpassserver.owner.owner.entity.Owner;
import com.example.fitpassserver.owner.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class OwnerAdminQueryServiceImpl implements OwnerAdminQueryService {

    private final OwnerRepository ownerRepository;

    @Override
    public OwnerAdminResponseDTO.OwnerPagesDTO getOwnersInfo(int page, int size, String searchType, String keyword) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Owner> ownerPage;

        if (!StringUtils.hasText(searchType) || !StringUtils.hasText(keyword)) {
            ownerPage = ownerRepository.findAll(pageRequest);
        } else {
            switch (searchType) {
                case "name":
                    ownerPage = ownerRepository.findByNameContaining(keyword, pageRequest);
                    break;
                case "loginId":
                    ownerPage = ownerRepository.findByLoginIdContaining(keyword, pageRequest);
                    break;
                case "phoneNumber":
                    ownerPage = ownerRepository.findByPhoneNumberContaining(keyword, pageRequest);
                    break;
                case "coporation":
                    ownerPage = ownerRepository.findByCorporationContaining(keyword, pageRequest);
                    break;
                default:
                    ownerPage = ownerRepository.findAll(pageRequest);
            }
        }

        return OwnerAdminConverter.toPageDTO(ownerPage);
    }
}
