package com.example.fitpassserver.admin.owner.service.command;

import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.owner.owner.entity.Owner;
import com.example.fitpassserver.owner.owner.entity.OwnerStatus;
import com.example.fitpassserver.owner.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OwnerAdminCommandServiceImpl implements OwnerAdminCommandService {
    private final OwnerRepository ownerRepository;

    @Override
    @Transactional
    public void patchOwnerStatus(String loginId, Boolean isApproval) {
        Owner owner = ownerRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        if (isApproval)
            owner.updateOwnerStatus(OwnerStatus.UNREGISTERED);
        else
            owner.updateOwnerStatus(OwnerStatus.STOPPED);
        log.info("loginId:" + loginId + "status:" + owner.ownerStatus);
    }
}
