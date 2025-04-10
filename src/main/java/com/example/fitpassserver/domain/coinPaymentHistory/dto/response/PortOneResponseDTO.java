package com.example.fitpassserver.domain.coinPaymentHistory.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PortOneResponseDTO {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SearchSinglePaymentDTO(
            @JsonProperty("status")
            String status,
            @JsonProperty("id")
            String id,
            @JsonProperty("orderName")
            String orderName,
            @JsonProperty("amount")
            PaymentAmount amount,
            @JsonProperty("customer")
            Customer customer
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PaymentAmount(
            @JsonProperty("total")
            int total,
            @JsonProperty("paid")
            int paid
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Customer(
            @JsonProperty("id")
            Long id,
            @JsonProperty("name")
            String name
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BillingKeyInfo(
            List<BillingKeyPaymentMethod> items
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BillingKeyPaymentMethod(
            String billingKey,
            List<BillingKeyPaymentMethodCard> methods
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BillingKeyPaymentMethodCard(
            @JsonProperty("type")
            String type,
            @JsonProperty("card")
            Card card
    ) {

    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Card(
            @JsonProperty("issuer")
            String issuer,
            @JsonProperty("type")
            String type,
            @JsonProperty("number")
            String number
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BillingKeyPaymentSummary(
            @JsonProperty("payment")
            Payment payment
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Payment(
            @JsonProperty("pgTxId")
            String pgTxId,
            @JsonProperty("paidAt")
            String paidAt
    ) {

    }

}
