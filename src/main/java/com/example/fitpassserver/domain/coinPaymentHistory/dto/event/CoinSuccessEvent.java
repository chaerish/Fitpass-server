package com.example.fitpassserver.domain.coinPaymentHistory.dto.event;

import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.member.entity.Member;

public record CoinSuccessEvent(
        Member member,
        KakaoPaymentApproveDTO dto

) {
}
