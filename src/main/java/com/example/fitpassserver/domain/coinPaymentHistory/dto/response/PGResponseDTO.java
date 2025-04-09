package com.example.fitpassserver.domain.coinPaymentHistory.dto.response;

import com.example.fitpassserver.domain.coin.entity.Coin;
import lombok.Builder;

import java.util.List;

public class PGResponseDTO {

    @Builder
    public record PGSinglePayResponseDTO(
            Long coinId
    ) {
        public static PGSinglePayResponseDTO from(Coin coin) {
            return PGSinglePayResponseDTO.builder()
                    .coinId(coin.getId())
                    .build();
        }
    }

    @Builder
    public record PGSearchCardResponseDTO(
            String billingKey,
            String bank,
            String type,
            String number
    ) {
        public static PGSearchCardResponseDTO from(String billingKey, String bank, String type, String number) {
            return PGSearchCardResponseDTO.builder()
                    .billingKey(billingKey)
                    .bank(bank)
                    .type(type)
                    .number(number)
                    .build();
        }
    }

    @Builder
    public record PGSearchCardListResponseDTO(
            List<PGSearchCardResponseDTO> items
    ) {
        public static PGSearchCardListResponseDTO of(PortOneResponseDTO.BillingKeyInfo dto) {
            return PGSearchCardListResponseDTO.builder()
                    .items(dto.items().stream().map(method -> {
                        PortOneResponseDTO.Card card = method.methods().get(0).card();
                        return PGSearchCardResponseDTO.from(method.billingKey(), card.issuer(), card.type(), card.number());
                    }).toList())
                    .build();
        }
    }

}
