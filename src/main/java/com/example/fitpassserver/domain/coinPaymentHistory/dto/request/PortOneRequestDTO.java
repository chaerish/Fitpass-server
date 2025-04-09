package com.example.fitpassserver.domain.coinPaymentHistory.dto.request;

import lombok.Builder;

import java.util.List;

public class PortOneRequestDTO {

    @Builder
    public record SearchCardRequestDTO(
            BillingKeySortInput sort,
            BillingKeyFilterInput filter
    ) {
        public static SearchCardRequestDTO from(Long customerId) {
            return SearchCardRequestDTO.builder()
                    .sort(BillingKeySortInput.defaultSort())
                    .filter(BillingKeyFilterInput.from(customerId))
                    .build();
        }
    }

    @Builder
    public record BillingKeyFilterInput(
            List<String> status,
            String customerId
    ) {
        public static BillingKeyFilterInput from(Long customerId) {
            return BillingKeyFilterInput.builder()
                    .status(List.of("ISSUED"))
                    .customerId(String.valueOf(customerId))
                    .build();
        }
    }

    public record BillingKeySortInput(
            String by,
            String order
    ) {
        public static BillingKeySortInput defaultSort() {
            return new BillingKeySortInput("ISSUED_AT", "DESC");
        }
    }

    @Builder
    public record BillingKeyPaymentRequestDTO(
            String billingKey,
            String orderName,
            Amount amount,
            Customer customer,
            String currency
    ) {
        public static BillingKeyPaymentRequestDTO from(Long customerId, String billingKey, String orderName, int total) {
            return BillingKeyPaymentRequestDTO.builder()
                    .customer(new Customer(String.valueOf(customerId)))
                    .billingKey(billingKey)
                    .orderName(orderName)
                    .amount(new Amount(total))
                    .currency("KRW")
                    .build();
        }
    }

    public record Amount(
            int total
    ) {

    }
    public record Customer(
            String id
    ) {

    }

//    public record PageInput(
//            int number,
//            int size
//    ) {
//    }
}
