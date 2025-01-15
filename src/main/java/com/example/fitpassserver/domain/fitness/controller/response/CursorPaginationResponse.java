package com.example.fitpassserver.domain.fitness.controller.response;

import java.util.List;

public class CursorPaginationResponse<T> {
    private final List<T> data; // 조회된 데이터 리스트
    private final Long nextCursor; // 다음 요청에 사용할 커서 값

    public CursorPaginationResponse(List<T> data, Long nextCursor) {
        this.data = data;
        this.nextCursor = nextCursor;
    }

    public List<T> getData() {
        return data;
    }
    public Long getNextCursor() {
        return nextCursor;
    }
}