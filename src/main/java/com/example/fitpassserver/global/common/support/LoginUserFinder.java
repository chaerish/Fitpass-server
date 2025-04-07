package com.example.fitpassserver.global.common.support;

import java.util.Optional;

public interface LoginUserFinder {
    Optional<? extends LoginUser> findByLoginId(String loginId);

    Optional<? extends LoginUser> findByNameAndPhoneNumber(String name, String phoneNumber);

    boolean existsByLoginId(String loginId);

    boolean existsByName(String name);

    boolean existsByPhoneNumber(String phoneNumber);
}
