package com.example.fitpassserver.domain.plan.dto.request;

public record SubscriptionRequestDTO(
        String cid,
        String sid,
        String partner_order_id,
        String user_order_id,
        String item_name,
        Integer quantity,
        Integer total_amount,
        Integer tax_free_amount
) {
}
