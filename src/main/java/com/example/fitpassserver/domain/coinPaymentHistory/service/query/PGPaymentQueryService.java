package com.example.fitpassserver.domain.coinPaymentHistory.service.query;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PGResponseDTO;
import com.example.fitpassserver.domain.member.entity.Member;

public interface PGPaymentQueryService {
    PGResponseDTO.PGSearchCardListResponseDTO findCards(Member member);
}
