package com.example.fitpassserver.domain.coinPaymentHistory.service.command;

import com.example.fitpassserver.domain.coin.entity.Coin;
import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PGRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PortOneRequestDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PGResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PortOneResponseDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.entity.CoinPaymentHistory;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.coinPaymentHistory.util.PortOneApiUtil;
import com.example.fitpassserver.domain.coinPaymentHistory.util.PortOnePaymentIdUtil;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.entity.PlanTypeEntity;
import com.example.fitpassserver.domain.plan.exception.PlanErrorCode;
import com.example.fitpassserver.domain.plan.exception.PlanException;
import com.example.fitpassserver.domain.plan.repository.PlanTypeRepository;
import com.example.fitpassserver.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PGPaymentCommandServiceImpl implements PGPaymentCommandService {

    private final PortOneApiUtil portOneApiUtil;
    private final CoinService coinService;
    private final CoinPaymentHistoryService coinPaymentHistoryService;
    private final PlanService planService;
    private final PlanTypeRepository planTypeRepository;


    @Override
    public PGResponseDTO.PGSinglePayResponseDTO payWithBillingKey(Member member, PGRequestDTO.PGPaymentWithBillingKeyRequestDTO dto) {
        String paymentId = PortOnePaymentIdUtil.getRandomPaymentId();
        Coin coin = coinService.createNewCoinByPg(member, paymentId, dto.amount());

        CoinPaymentHistory coinPaymentHistory = coinPaymentHistoryService.createPGSinglePayCoin(member, paymentId, dto.amount(), coin);
        coinService.setCoinAndCoinPayment(coin, coinPaymentHistory);

        payWithBillingKey(member, paymentId, dto);
        return PGResponseDTO.PGSinglePayResponseDTO.from(coin);
    }

    @Override
    public PGResponseDTO.PGSinglePayResponseDTO pgSinglePay(Member member, PGRequestDTO.PGSinglePayRequestDTO dto) {
        PortOneResponseDTO.SearchSinglePaymentDTO searchSinglePaymentDTO = portOneApiUtil.searchSinglePayment(dto.paymentId());
        Coin coin = coinService.createNewCoinByPg(member, dto.paymentId(), searchSinglePaymentDTO.amount().paid());

        CoinPaymentHistory coinPaymentHistory = coinPaymentHistoryService.createPGSinglePayCoin(member, dto.paymentId(), searchSinglePaymentDTO.amount().paid(), coin);
        coinService.setCoinAndCoinPayment(coin, coinPaymentHistory);

        return PGResponseDTO.PGSinglePayResponseDTO.from(coin);
    }

    @Override
    public PGResponseDTO.PGSubscriptionPayResponseDTO createPGSubscriptionPay(Member member, PGRequestDTO.PGSubscriptionPaymentWithBillingKeyRequestDTO dto) {
        String paymentId = PortOnePaymentIdUtil.getRandomPaymentId();
        planService.checkOriginalPlan(member);

        Plan plan = planService.createNewPGPlan(member, dto);
        Coin coin = coinService.createSubscriptionNewCoin(member, plan);
        CoinPaymentHistory coinPaymentHistory = coinPaymentHistoryService.createPGPlanPayCoin(member, dto.orderName(), paymentId, dto.amount(), coin);
        PortOneRequestDTO.BillingKeyPaymentRequestDTO request = PortOneRequestDTO.BillingKeyPaymentRequestDTO.from(member.getId(), dto.billingKey(), dto.orderName(), dto.amount());
        portOneApiUtil.payWithBillingKey(paymentId, request);
        coinService.setCoinAndCoinPayment(coin, coinPaymentHistory);

        return PGResponseDTO.PGSubscriptionPayResponseDTO.from(coin);
    }

    @Override
    public void paySubscription(Plan plan) {
        String paymentId = PortOnePaymentIdUtil.getRandomPaymentId();
        Member member = plan.getMember();
        PlanTypeEntity planType = planTypeRepository.findByPlanType(plan.getPlanType()).orElseThrow(() ->
                new PlanException(PlanErrorCode.PLAN_NOT_FOUND));
        PortOneRequestDTO.BillingKeyPaymentRequestDTO request = PortOneRequestDTO.BillingKeyPaymentRequestDTO.from(member.getId(), plan.getSid(), planType.getPlanType().getName(), planType.getCoinQuantity() + planType.getCoinAddition());
        portOneApiUtil.payWithBillingKey(paymentId, request);
        coinService.createSubscriptionNewCoin(member, plan);
    }

    private PortOneResponseDTO.BillingKeyPaymentSummary payWithBillingKey(Member member, String paymentId, PGRequestDTO.PGPaymentWithBillingKeyRequestDTO dto) {
        PortOneRequestDTO.BillingKeyPaymentRequestDTO request = PortOneRequestDTO.BillingKeyPaymentRequestDTO.from(member.getId(), dto.billingKey(), dto.orderName(), dto.amount());
        PortOneResponseDTO.BillingKeyPaymentSummary response = portOneApiUtil.payWithBillingKey(paymentId, request);
        return response;
    }
}
