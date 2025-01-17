package com.example.fitpassserver.domain.plan.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PlanSubscriptionResponseDTO(
        String aid,
        String tid,
        String cid,
        String sid,
        String partner_order_id,
        String partner_user_id,
        @JsonProperty("payment_method_type")
        String paymentMethodType,
        @JsonProperty("item_name")
        String itemName,
        @JsonProperty("quantity")
        int quantity,
        @JsonProperty("amount")
        Amount amount,
        @JsonProperty("card_info")
        CardInfo cardInfo,
        @JsonProperty("created_at")
        String createdAt,
        @JsonProperty("approved_at")
        String approvedAt,
        @JsonProperty("payload")
        String payload,
        @JsonProperty("sequential_payment_methods")
        List<Sequential> sequential // 변경: List로 처리
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

    public record CardInfo(
            @JsonProperty("total") int total,
            @JsonProperty("tax_free") int taxFree,
            @JsonProperty("vat") int vat,
            @JsonProperty("point") int point,
            @JsonProperty("discount") int discount,
            @JsonProperty("green_deposit") int greenDeposit
    ) {
    }

    public record Sequential(
            @JsonProperty("payment_priority")
            Integer paymentPriority,
            @JsonProperty("payment_method_type")
            String paymentMethodType,
            String sid,
            @JsonProperty("card_info")
            CardInfo2 cardInfo2
    ) {
    }

    public record CardInfo2(
            @JsonProperty("kakaopay_purchase_corp")
            String kakaoPayPurchaseCorp,
            @JsonProperty("kakaopay_purchase_corp_code")
            String kakaoPayPurchaseCorpCode,
            @JsonProperty("kakaopay_issuer_corp")
            String kakaoPayIssuerCorp,
            @JsonProperty("kakaopay_issuer_corp_code")
            String kakaoPayIssuerCorpCode,
            String bin,
            @JsonProperty("card_type")
            String cardType
    ) {
    }
}

