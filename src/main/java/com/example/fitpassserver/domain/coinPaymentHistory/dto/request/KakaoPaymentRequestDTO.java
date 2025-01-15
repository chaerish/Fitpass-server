package com.example.fitpassserver.domain.coinPaymentHistory.dto.request;


public record KakaoPaymentRequestDTO(
        String cid,
        String partner_order_id,
        String partner_user_id,
        String item_name,
        Integer quantity,
        Integer total_amount,
        Integer tax_free_amount,
        String approval_url,
        String cancel_url,
        String fail_url
) {
}