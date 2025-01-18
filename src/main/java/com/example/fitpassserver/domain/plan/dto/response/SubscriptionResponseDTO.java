package com.example.fitpassserver.domain.plan.dto.response;

public record SubscriptionResponseDTO(
        String aid,
        String cid,
        String sid,
        String tid,
        String partner_order_id,
        String partner_user_id,
        String payment_method_type,
        String item_name,
        int quantity,
        Amount amount,
        String created_at,
        String approved_at
) {
    public record Amount(
            int total,
            int tax_free,
            int vat,
            int green_deposit
    ) {
    }
}

