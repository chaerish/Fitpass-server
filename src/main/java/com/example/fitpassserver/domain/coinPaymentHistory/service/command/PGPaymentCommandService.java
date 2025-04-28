package com.example.fitpassserver.domain.coinPaymentHistory.service.command;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PGRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PGResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.entity.Plan;

public interface PGPaymentCommandService {
    PGResponseDTO.PGSinglePayResponseDTO payWithBillingKey(Member member, PGRequestDTO.PGPaymentWithBillingKeyRequestDTO dto);
    PGResponseDTO.PGSinglePayResponseDTO pgSinglePay(Member member, PGRequestDTO.PGSinglePayRequestDTO dto);

    PGResponseDTO.PGSubscriptionPayResponseDTO createPGSubscriptionPay(Member member, PGRequestDTO.PGSubscriptionPaymentWithBillingKeyRequestDTO dto);
    void paySubscription(Plan plan);
}
