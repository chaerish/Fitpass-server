package com.example.fitpassserver.domain.coinPaymentHistory.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoPaymentApproveDTO(
        String aid,
        String tid,
        String cid,
        String sid,
        @JsonProperty("partner_order_id")
        String partnerOrderId,
        @JsonProperty("partner_user_id")
        String partnerUserId,
        @JsonProperty("payment_method_type")
        String paymentMethodType,
        @JsonProperty("item_name")
        String itemName,
        int quantity,
        @JsonProperty("amount")
        Amount amount,
        @JsonProperty("created_at")
        String createdAt,
        @JsonProperty("approved_at")
        String approvedAt,
        @JsonProperty("payload")
        String payload
) {
    public record Amount(
            @JsonProperty("total") int total,
            @JsonProperty("tax_free") int taxFree,
            @JsonProperty("vat") int vat,
            @JsonProperty("point") int point,
            @JsonProperty("discount") int discount,
            @JsonProperty("green_deposit") int greenDeposit
    ) {
    }
}
