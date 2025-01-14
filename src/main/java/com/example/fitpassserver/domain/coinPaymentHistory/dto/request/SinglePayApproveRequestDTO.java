package com.example.fitpassserver.domain.coinPaymentHistory.dto.request;

public record SinglePayApproveRequestDTO(
        String cid,
        String tid,
        String partner_order_id,
        String partner_user_id,
        String pg_token
) {
}