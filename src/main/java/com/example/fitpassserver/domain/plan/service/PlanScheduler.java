package com.example.fitpassserver.domain.plan.service;

import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.coinPaymentHistory.service.KakaoPaymentService;
import com.example.fitpassserver.domain.member.entity.Member;
import com.example.fitpassserver.domain.plan.dto.response.SubscriptionResponseDTO;
import com.example.fitpassserver.domain.plan.entity.Plan;
import com.example.fitpassserver.domain.plan.repository.PlanRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlanScheduler {
    private final PlanRepository planRepository;
    private final KakaoPaymentService paymentService;
    private final CoinPaymentHistoryService coinPaymentHistoryService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void regularPay() {
        List<Plan> planToProcess = planRepository.findAllByPlanDateBefore(LocalDate.now());
        for (Plan plan : planToProcess) {
            try {
                processPay(plan);
            } catch (Exception e) {
                log.error("plan ID: {} 에서 정기 결제 실패: {}", plan.getId(), e.getMessage(), e);
            }
        }
    }

    @Transactional
    public void processPay(Plan plan) {
        Member member = plan.getMember();
        SubscriptionResponseDTO response = paymentService.ready(plan);
        coinPaymentHistoryService.createNewCoinHistory(member, response.tid(), response.amount().total());
        plan.updatePlanDate();
        plan.addPaymentCount();
        planRepository.save(plan);
    }

}
