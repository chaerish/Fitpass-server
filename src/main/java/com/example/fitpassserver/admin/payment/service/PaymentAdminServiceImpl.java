package com.example.fitpassserver.admin.payment.service;

import com.example.fitpassserver.admin.payment.converter.PaymentHistoryAdminConverter;
import com.example.fitpassserver.admin.payment.dto.response.CoinPaymentHistoryResponseDTO;
import com.example.fitpassserver.admin.payment.dto.response.PassPaymentHistoryResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.repository.CoinPaymentRepository;
import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import com.example.fitpassserver.domain.fitness.repository.MemberFitnessRepository;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentAdminServiceImpl implements PaymentAdminService {
    private final CoinPaymentRepository coinPaymentRepository;
    private final String PASS_TYPE = "패스";
    private final String COIN_TYPE = "코인";
    private final MemberRepository memberRepository;
    private final MemberFitnessRepository memberFitnessRepository;

    @Override
    public CoinPaymentHistoryResponseDTO getCoinPaymentHistory(String memberName, int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CoinPaymentHistory> coinPaymentHistoryPage;
        if (memberName != null) { //검색기능
            coinPaymentHistoryPage = coinPaymentRepository.findAllByMemberIn(pageable, getMember(memberName));
        } else {
            coinPaymentHistoryPage = coinPaymentRepository.findAll(pageable);
        }
        return PaymentHistoryAdminConverter.toCoinPaymentResponseDTO(coinPaymentHistoryPage);
    }

    @Override
    public PassPaymentHistoryResponseDTO getPassPaymentHistory(String memberName, int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<MemberFitness> memberFitnessPage;
        if (memberName != null) { //검색기능
            memberFitnessPage = memberFitnessRepository.findAllByMemberIn(pageable, getMember(memberName));
        } else {
            memberFitnessPage = memberFitnessRepository.findAll(pageable);
        }
        return PaymentHistoryAdminConverter.toPassPaymentResponseDTO(memberFitnessPage);
    }

    private List<Member> getMember(String memberName) {
        return memberRepository.findAllByName(memberName);
    }
}
