package com.example.fitpassserver.domain.coinPaymentHistory.service.command;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PGRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PortOneResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;

public interface PGPaymentCommandService {
    PortOneResponseDTO.BillingKeyPaymentSummary payWithBillingKey(Member member, String paymentId, PGRequestDTO.PGPaymentWithBillingKeyRequestDTO dto);
}
