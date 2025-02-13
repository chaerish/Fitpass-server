package com.example.fitpassserver.admin.payment.converter;

import com.example.fitpassserver.admin.payment.dto.response.CoinPaymentHistoryResponseDTO;
import com.example.fitpassserver.admin.payment.dto.response.PassPaymentHistoryResponseDTO;
import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.entity.PlanType;
import java.util.List;
import org.springframework.data.domain.Page;

public class PaymentHistoryAdminConverter {
    public static CoinPaymentHistoryResponseDTO toCoinPaymentResponseDTO(
            Page<CoinPaymentHistory> coinPaymentHistories) {
        List<CoinPaymentHistoryResponseDTO.PaymentHistoryDTO> content = coinPaymentHistories.getContent().stream()
                .map(history -> {
                    Member member = history.getMember();
                    Coin coin = history.getCoin();
                    PlanType plan = coin.getPlanType();
                    return CoinPaymentHistoryResponseDTO.PaymentHistoryDTO.builder()
                            .id(history.getId())
                            .memberName(member.getName())
                            .account(member.getLoginId())
                            .phoneNumber(member.getPhoneNumber())
                            .createdAt(history.getCreatedAt())
                            .planType(plan.equals(PlanType.NONE) ? coin.getCount() + "코인" : plan.getName())
                            .price(history.getPaymentPrice())
                            .build();
                }).toList();
        return CoinPaymentHistoryResponseDTO.builder()
                .content(content)
                .size(coinPaymentHistories.getSize())
                .totalElements(coinPaymentHistories.getTotalElements())
                .totalPages(coinPaymentHistories.getTotalPages())
                .number(coinPaymentHistories.getNumber())
                .build();
    }

    public static PassPaymentHistoryResponseDTO toPassPaymentResponseDTO(
            Page<MemberFitness> memberFitnesses) {
        List<PassPaymentHistoryResponseDTO.PaymentHistoryDTO> content = memberFitnesses.getContent().stream()
                .map(pass -> {
                    Member member = pass.getMember();
                    return PassPaymentHistoryResponseDTO.PaymentHistoryDTO.builder()
                            .id(pass.getId())
                            .memberName(member.getName())
                            .account(member.getLoginId())
                            .phoneNumber(member.getPhoneNumber())
                            .fitnessName(pass.getFitness().getName())
                            .coinCount(pass.getFitness().getTotalFee())
                            .createdAt(pass.getCreatedAt())
                            .passStatus(pass.getStatus().getValue())
                            .activeTime(pass.getActiveTime())
                            .build();
                }).toList();
        return PassPaymentHistoryResponseDTO.builder()
                .content(content)
                .size(memberFitnesses.getSize())
                .totalElements(memberFitnesses.getTotalElements())
                .totalPages(memberFitnesses.getTotalPages())
                .number(memberFitnesses.getNumber())
                .build();
    }
}