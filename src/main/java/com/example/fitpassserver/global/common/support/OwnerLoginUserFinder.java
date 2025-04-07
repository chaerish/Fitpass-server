package com.example.fitpassserver.global.common.support;

import com.example.fitpassserver.owner.owner.entity.Owner;
import com.example.fitpassserver.owner.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OwnerLoginUserFinder implements LoginUserFinder {
    private final OwnerRepository ownerRepository;

    @Override
    public Optional<Owner> findByLoginId(String loginId) {
        return ownerRepository.findByLoginId(loginId);
    }

    @Override
    public Optional<Owner> findByNameAndPhoneNumber(String name, String phoneNumber) {
        return ownerRepository.findByNameAndPhoneNumber(name, phoneNumber);
    }

    @Override
    public boolean existsByLoginId(String loginId) {
        return ownerRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean existsByName(String name) {
        return ownerRepository.existsByName(name);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return ownerRepository.existsByPhoneNumber(phoneNumber);
    }
}
