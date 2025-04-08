package com.example.fitpassserver.domain.coinPaymentHistory.service.query;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PortOneRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PGResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PortOneResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.util.PortOneApiUtil;
import com.example.fitpassserver.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PGPaymentQueryServiceImpl implements PGPaymentQueryService {

    private final PortOneApiUtil portOneApiUtil;

    @Override
    public PGResponseDTO.PGSearchCardListResponseDTO findCards(Member member) {
        PortOneRequestDTO.SearchCardRequestDTO dto = PortOneRequestDTO.SearchCardRequestDTO.from(member.getId());
        PortOneResponseDTO.BillingKeyInfo info = portOneApiUtil.searchCards(dto);
        return PGResponseDTO.PGSearchCardListResponseDTO.of(info);
    }
}
