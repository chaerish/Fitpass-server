package com.example.fitpassserver.domain.fitness.entity;

public enum Status {
    NONE("사용 안함"), PROGRESS("사용 중"), DONE("사용 불가"), REVIEWED("사용 불가(리뷰 작성 완료)");

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;
}
