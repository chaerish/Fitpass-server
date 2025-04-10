package com.example.fitpassserver.global.common.support;

import com.example.fitpassserver.domain.member.entity.Role;

public interface LoginUser {
    Long getId();

    String getLoginId();

    String getPassword();

    String getPhoneNumber();

    Role getRole();

    String getName();

}
