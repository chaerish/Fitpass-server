package com.example.fitpassserver.admin.payment.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record CoinPaymentHistoryResponseDTO(
        List<PaymentHistoryDTO> content,
        long totalElements, //전체 갯수
        int totalPages, //총 페이지 수
        int size, //현재 페이지 크기
        int number //현재 페이지 번호

) {
    @Builder
    public record PaymentHistoryDTO(
            Long id,
            String memberName,
            String account,
            String phoneNumber,
            LocalDateTime createdAt,
            String planType,
            int price
    ) {

    }
}
