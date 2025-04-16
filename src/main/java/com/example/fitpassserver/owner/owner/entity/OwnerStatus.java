package com.example.fitpassserver.owner.owner.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OwnerStatus {
    OPERATION("운영중"),
    UNREGISTERED("미등록"),
    PENDING("승인대기"),
    STOPPED("정지");

    private final String key;
}
