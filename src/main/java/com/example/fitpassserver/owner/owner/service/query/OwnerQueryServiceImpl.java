package com.example.fitpassserver.owner.owner.service.query;

import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.owner.owner.entity.Owner;
import com.example.fitpassserver.owner.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerQueryServiceImpl implements OwnerQueryService {
    private final OwnerRepository ownerRepository;

    public Owner getOwner(String loginId) {
        return ownerRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
    }

}
