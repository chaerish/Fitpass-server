package com.example.fitpassserver.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST"),
    MEMBER("ROLE_MEMBER"),
    OWNER("ROLE_OWNER"),
    ADMIN("ROLE_ADMIN");

    private final String key;
}
