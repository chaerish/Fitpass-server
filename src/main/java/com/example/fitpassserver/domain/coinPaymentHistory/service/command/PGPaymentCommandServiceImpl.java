package com.example.fitpassserver.domain.coinPaymentHistory.service.command;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PGRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PortOneRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PortOneResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.util.PortOneApiUtil;
import com.example.fitpassserver.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PGPaymentCommandServiceImpl implements PGPaymentCommandService {

    private final PortOneApiUtil portOneApiUtil;

    @Override
    public PortOneResponseDTO.BillingKeyPaymentSummary payWithBillingKey(Member member, String paymentId, PGRequestDTO.PGPaymentWithBillingKeyRequestDTO dto) {
        PortOneRequestDTO.BillingKeyPaymentRequestDTO request = PortOneRequestDTO.BillingKeyPaymentRequestDTO.from(member.getId(), dto.billingKey(), dto.orderName(), dto.amount());
        PortOneResponseDTO.BillingKeyPaymentSummary response = portOneApiUtil.payWithBillingKey(paymentId, request);
        return response;
    }
}
